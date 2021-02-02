package com.projects.biglogs.dto

case class Cookies(cookie:String, timestamp:String, date:String){
  def sameDate(day:String) = day.equals(date)
}
