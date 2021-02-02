package com.projects.biglogs.util

import org.apache.spark.sql.SparkSession

object SparkBuilder {
  val spark = SparkSession.builder()
    .appName("logger")
    .config("spark.master","local")
    //.config("spark.sql.session.timeZone","UTC")
    .getOrCreate()
}
