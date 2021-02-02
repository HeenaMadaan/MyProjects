package com.projects.logs.exception

case class TrackerExecutionException(msg:String, ex:Throwable=null) extends RuntimeException(msg,ex)
