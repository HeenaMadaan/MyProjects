package com.projects.biglogs.sink

import org.apache.spark.sql.DataFrame

case class ConsoleWriter() extends Writer{
  override def write(df:DataFrame, path:String=null)= {

    df.write.format("Console").mode("append").save
  }
}
