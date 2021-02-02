package com.projects.logs.source

import java.io.{BufferedReader, IOException}

import com.projects.logs.dto.Row
import com.projects.logs.exception.TrackerException

case class FlatFileInputReader(DEFAULT_DELIMITER:String = ",") extends BaseInputReader {

  @throws(classOf[TrackerException])
  override def read(file:String, colDel:String) = {
    try{
      val br:BufferedReader=reader(file)
      val header  = Option(br.readLine()).getOrElse(throw TrackerException("File doesn't have headers"))
      val headers = super.headers(header,colDel)
      toRows(br,headers,colDel)
    }
    catch {
      case e:IOException => e.printStackTrace()
        throw TrackerException("Exception while reading input", e)
    }
  }

  @throws(classOf[TrackerException])
  override def read (file:String) = read(file,DEFAULT_DELIMITER)

  @throws(classOf[TrackerException])
  override def read(file: String, headers: Array[String], delimiter: String): List[Row] = {
    try {
      val br= reader(file)
      toRows(br,headers,delimiter)
    }catch{
      case e:IOException => e.printStackTrace()
        throw TrackerException("Exception while reading input",e)
    }
  }

  @throws(classOf[TrackerException])
  override def read(file: String, headers: Array[String]): List[Row] = read(file,headers,DEFAULT_DELIMITER)
}
