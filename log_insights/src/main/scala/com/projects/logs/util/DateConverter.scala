package com.projects.logs.util

import java.text.{DateFormat, ParseException, SimpleDateFormat}

class DateConverter(origFormat:DateFormat, targetFormat:DateFormat){
  @throws(classOf[ParseException])
  def format(date:String) = targetFormat.format(origFormat.parse(date))
}

object DateConverter {
  val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
  val OUTPUT_DATE_FORMAT = "yyyy-MM-dd"

  def apply(origFormat: String, targetFormat: String): DateConverter = new DateConverter(new SimpleDateFormat(origFormat), new SimpleDateFormat(targetFormat))

}
