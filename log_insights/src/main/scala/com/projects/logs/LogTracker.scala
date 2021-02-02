package com.projects.logs

import com.projects.logs.config.ActivityConfig
import com.projects.logs.dto.Cookies
import com.projects.logs.exception.TrackerException
import com.projects.logs.sink.ConsoleWriter
import com.projects.logs.source.{FlatFileInputReader, InputReader}
import com.projects.logs.util.{CLIConfig, ConfigParser, RowParser}

class LogTracker(inputReader: InputReader, writer:ConsoleWriter[String], config:ActivityConfig ){

  @throws(classOf[TrackerException])
  def track() ={  // convert file input into cookie objects
    val files: List[String] = config.file
    val filteredCookies = files.flatMap(getCookie(_))
    val activeCookies = getActiveCookies(filteredCookies)
    writer.write(activeCookies)
  }

  private def getCookie(file:String)  = {
    try{
      inputReader.read(file).map(RowParser.toCookies(_)).filter(_.isSameDate(config.date))
    } catch {
      case e:TrackerException => throw new RuntimeException("unable to process file" + file, e)
    }
  }

  private def getActiveCookies(cookies:List[Cookies]) = {
    val cookiesCountMap =  cookies.groupBy(_.cookies).mapValues(_.size)
    val maxCount = if(cookiesCountMap.nonEmpty) Some(cookiesCountMap.values.max) else None
    cookiesCountMap.filter(_._2==maxCount.getOrElse(-1)).keys.toList
  }
}
object LogTracker{
  def main(args: Array[String]): Unit = {
    val config: ActivityConfig = ConfigParser.parse(args) // build config file
    new LogTracker(FlatFileInputReader(),ConsoleWriter(),config).track()  // find active cookies in logs
  }
}
