package com.projects.biglogs.sink

import org.apache.spark.sql.DataFrame

abstract class Writer{
  def write(df:DataFrame, path:String)
}
