package com.projects.biglogs.util

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object RecordGenerator {

  def createFile(records:List[TestRecord], basePath :String, spark:SparkSession)  = {
    //val path = System.currentTimeMillis()
    val schema= StructType(
          Array(
            StructField("cookie",StringType),
            StructField("timestamp",StringType)
          )
        )
    val df = spark.createDataFrame(records).toDF("cookie","timestamp")
    //val tempPath = new Path(s"$basePath/${System.currentTimeMillis()}") /home/milap/Documents/Projects/biglogsinsights/src/test/resources/input
    df.write.format("csv").option("header","true").save(basePath)
  System.out.println("")
    //tempPath.toString
  }
}
