package com.projects.biglogs.util

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import org.json4s.jackson.JsonMethods.parse
import org.json4s.DefaultFormats

import scala.io.Source

object ConfigParser {

  implicit val jformats : DefaultFormats.type = DefaultFormats

  def filesystem (spark:SparkSession) = FileSystem.get(spark.sparkContext.hadoopConfiguration)

  def getConfig[T] (sparkSession: SparkSession, path:String)(implicit m:Manifest[T]) = {
    val fs = filesystem(sparkSession)
    val content = Source.fromInputStream(fs.open(new Path(path))).getLines().mkString
    parse(content).extract[T]
  }
}
