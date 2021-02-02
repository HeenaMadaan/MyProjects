package com.projects.biglogs

import com.projects.biglogs.config.{AppConfig, CLIConfig}
import com.projects.biglogs.dto.Cookies
import com.projects.biglogs.sink.{ConsoleWriter, FileWriter}
import com.projects.biglogs.util.{CLIParser, ConfigParser, SparkBuilder}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object LogTracker {
  def analyze(spark: SparkSession, config: AppConfig, date:String, batchId:Long)={
    //    val cookieSchema = StructType(
    //      Array(
    //        StructField("cookie",StringType),
    //        StructField("timestamp",StringType)
    //      )
    //    )
    val cookieDF: DataFrame = spark.read.format("csv")
      .option("inferSchema","true")
      .option("header","true")
      .option("timestampFormat","yyyy-MM-dd")
      .option("spark.sql.session.timeZone", "UTC")
      //.schema(cookieSchema)
      .load(s"${config.inputPath}/*.csv")
    val extendedCookieDF = cookieDF.withColumn("date",to_date(col("timestamp")))
    import spark.implicits._
    val cookieDS = extendedCookieDF.as[Cookies]
    val filteredCookies = cookieDS.filter(c=>c.sameDate(date))
    val cookiesCount = filteredCookies.groupBy(col("cookie")).count()
    val maxCookieCount = cookiesCount.agg(max("count")).head().getLong(0)
    val activeCookies = cookiesCount.where(col("count")===maxCookieCount).select("cookie")
    FileWriter().write(activeCookies, s"${config.outputPath}/batch$batchId")
  }
  def main(args: Array[String]): Unit = {
    val config = CLIParser.parse(args)
    val spark = SparkBuilder.spark
    val batchId:Long = System.currentTimeMillis()
    val activityConfig = ConfigParser.getConfig[AppConfig](spark, config.path)
    analyze(spark,activityConfig,config.date, batchId)
  }
}
