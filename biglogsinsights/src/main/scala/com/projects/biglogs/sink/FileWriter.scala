package com.projects.biglogs.sink

import org.apache.spark.sql.DataFrame

case class FileWriter() extends Writer {
  override def write(df: DataFrame, path:String): Unit = {
    //val outputPath = s"${fs.getWorkingDirectory}/src/main/resources/output/file${System.currentTimeMillis()}"
    df.write.format("csv").option("header","true").save(path)
  }
}