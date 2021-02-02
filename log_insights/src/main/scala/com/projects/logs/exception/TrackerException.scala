package com.projects.logs.exception

case class TrackerException(msg:String,ex:Throwable=null) extends Exception(msg,ex) {}