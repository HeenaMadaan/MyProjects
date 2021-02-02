package com.projects.logs.util

import java.text.ParseException

import com.projects.logs.dto.{Cookies, Row}
import com.projects.logs.exception.TrackerExecutionException


object RowParser {
  val dateConverter = DateConverter(DateConverter.INPUT_DATE_FORMAT, DateConverter.OUTPUT_DATE_FORMAT)

  @throws(classOf[RuntimeException])
  def toCookies(row:Row) = {
    val ts = row.getAs(1)
    try{
      Cookies(row.getAs(0),ts, dateConverter.format(ts))
    }catch {
      case e:ParseException => throw TrackerExecutionException("unable to parse date format ", e)
    }
  }
}
