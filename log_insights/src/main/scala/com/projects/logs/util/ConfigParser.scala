package com.projects.logs.util

import java.text.{ParseException, SimpleDateFormat}

import com.projects.logs.config.ActivityConfig


object ConfigParser{
  val parser = new scopt.OptionParser[CLIConfig]("scopt"){
    head("scopt", "3.x")

    opt[String]('f',"file") required() action{
      (x,c) => c.copy(file=x)
      } validate {f=>
      if(f.length>0) success else failure("Must provide file name")
    }text "input file is required"


    opt[String]('d',"date") required() action{
      (x,c)=> c.copy(date = x)
    } validate{ d=>
      if(dateCheck(d)) success else failure("date must be in the format yyyy-mm-dd")
    }text "date is required"
  }

  def dateCheck(str: String)={
    isValidFormat(str) && isValidDate(str)
  }
  def isValidFormat(date:String) = date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")

  def isValidDate(date:String) = {
    val format = new SimpleDateFormat(DateConverter.OUTPUT_DATE_FORMAT)
    try{
      format.parse(date)
      true
    } catch {
      case e:ParseException => false
    }
  }
  def parse(args:Array[String]) ={
    val conf = parser.parse(args,CLIConfig()).getOrElse{
      throw new Exception("Missing arguments, pass both file/s and date")
    }
    ActivityConfig(conf)
  }
}
