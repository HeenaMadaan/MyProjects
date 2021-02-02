package com.projects.logs.source

import com.projects.logs.dto.Row
import com.projects.logs.exception.TrackerException

abstract class InputReader {
  @throws(classOf[TrackerException])
  def read(file:String, delimiter:String):List[Row]

  @throws(classOf[TrackerException])
  def read(file:String):List[Row]

  @throws(classOf[TrackerException])
  def read(file:String, headers: Array[String], delimiter:String):List[Row]

  @throws(classOf[TrackerException])
  def read(file:String, headers: Array[String]):List[Row]
}
