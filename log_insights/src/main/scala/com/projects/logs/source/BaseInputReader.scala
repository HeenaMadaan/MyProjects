package com.projects.logs.source

import java.io.{BufferedReader, File, FileNotFoundException, FileReader, IOException, InputStreamReader}
import java.util.stream.Collectors

import com.projects.logs.dto.{NamedField, Row}
import com.projects.logs.exception.{TrackerException, TrackerExecutionException}

import scala.collection.JavaConverters._


abstract class BaseInputReader extends InputReader {
  @throws(classOf[TrackerException])
  protected def reader(path: String) = {
    val file = new File(path)
    if (file.isAbsolute) {
      readFromAbsolutePath(file)
    }
    else {
      readAsResourcesStream(file)
    }
  }

  protected def readAsResourcesStream(file: File) = {
    try {
      new BufferedReader(new InputStreamReader(getClass.getResourceAsStream("/" + file.getPath)))
      //getClass().getResourceAsStream("Path relative to the current class")-allow to load a file as a stream based on a path relative to the class from which you call
    }catch{
      case e : RuntimeException => throw TrackerExecutionException("No file found " + file.getPath, e.getCause)
    }
  }

  @throws(classOf[TrackerException])
  protected def readFromAbsolutePath(file: File) = {
    try {
      new BufferedReader(new FileReader(file))
    }
    catch {
      case e: FileNotFoundException => throw TrackerException("Unable to read input file" + file.getPath, e.getCause)
    }
  }

  protected def headers(header: String, del: String) = header.split(del)

  @throws(classOf[IOException])
  protected def toRows(br:BufferedReader, headers:Array[String], colDel:String): List[Row] = {
    br.lines().collect(Collectors.toList[String]).asScala.toList.map(r=>formRow(r,colDel,headers))
    // import java convertors to transform java structures to scala or vice versa
  }

  private def formRow(str: String, del:String, headers:Array[String]): Row ={
    val cols = str.split(del)
    val namedFields = cols.zipWithIndex.map(r=>NamedField(headers(r._2),cols(r._2))).toList
    Row(namedFields)
  }
}