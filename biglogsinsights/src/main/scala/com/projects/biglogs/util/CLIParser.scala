package com.projects.biglogs.util

import java.text.{ParseException, SimpleDateFormat}

import com.projects.biglogs.config.CLIConfig
import org.apache.hadoop.fs.{FileSystem, Path}



object CLIParser {
    val parser = new scopt.OptionParser[CLIConfig]("scopt") {
      head("scopt","3.x")

      opt[String]('p',"path") required() action{
        (x,c) => c.copy(path=x)
      } validate { p =>
        if (validDir(p)) success else failure("file not specified")
      }text "input file is required"

      opt[String]('d', "date") required() action{
        (x,c) => c.copy(date=x)
      } validate { d =>
        if(dateCheck(d)) success else failure("incorrect date")
      }text "date is required property"
    }

  def dateCheck(date:String)= validDate(date) && validFormat(date)

  def validDate(date:String) = date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")

  def validFormat(date:String) = {
    val OUTPUT_DATE_FORMAT = "yyyy-MM-dd"
    val format = new SimpleDateFormat(OUTPUT_DATE_FORMAT)
    try{
      format.parse(date)
      true
    } catch{
      case e:ParseException => false
    }
  }

  def validDir (path:String) = {
    if(path==null || path.trim.isEmpty) false
    else {
      val dir = new Path(path)
      val sc = SparkBuilder.spark
      val fs = FileSystem.get(sc.sparkContext.hadoopConfiguration)
      //fs.isDirectory(dir)
      fs.isFile(dir)
    }
  }

    def parse(args:Array[String]) = {
      parser.parse(args, CLIConfig()).getOrElse{
        throw new Exception("Missing arguments, pass both file and date")
      }
    }
}
