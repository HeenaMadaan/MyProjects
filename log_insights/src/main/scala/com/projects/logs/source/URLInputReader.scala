package com.projects.logs.source
import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.URL

import com.projects.logs.dto.Row
import com.projects.logs.exception.TrackerException

class URLInputReader extends BaseInputReader {
  @throws(classOf[TrackerException])
  override def read(file: String, delimiter: String): List[Row] = {
    try{
      val br:BufferedReader = new BufferedReader(new InputStreamReader(new URL(file).openStream()))
      val header = Option(br.readLine()).getOrElse(throw TrackerException("File doesn't have headers"))
      val headers = super.headers(header, delimiter)
      toRows(br,headers, delimiter)
    }catch{
      case e:IOException => throw TrackerException("Exception while reading input",e)
    }
  }

  override def read(file:String):List[Row] = null

  override def read(file: String, headers: Array[String]): List[Row] = null

  override def read(file: String, headers: Array[String], delimiter: String): List[Row] = null
}
