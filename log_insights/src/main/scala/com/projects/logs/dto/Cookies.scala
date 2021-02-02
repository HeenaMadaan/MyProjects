package com.projects.logs.dto

case class Cookies(cookies:String, ts:String, date:String){
  def isSameDate(day:String) : Boolean = this.date.equals(day)
}
