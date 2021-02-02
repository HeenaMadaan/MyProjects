package com.projects.logs

import java.io.{ByteArrayOutputStream, IOException}

import com.projects.logs.exception.{TrackerException, TrackerExecutionException}
import com.projects.logs.util.{RecordGenerator, TestRecord}
import org.apache.commons.lang3.StringUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should._

class LogTrackerTest extends AnyFunSuite with BeforeAndAfter with Matchers{

  test("Should return one most active cookie"){
    val outContent = new ByteArrayOutputStream()
    val records = Array(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00"),
      TestRecord("5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00"),
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00"),
      TestRecord("fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00")
    ).toList
    Console.withOut(outContent) {
      process(records, "2018-12-09")
    }
    val result = StringUtils.trim(outContent.toString)
    assert(StringUtils.trim(outContent.toString) == "AtY0laUfhglK3lC7","Output not matching")
  }

  test("Should return multiple active cookies"){
    val outContent = new ByteArrayOutputStream()
    val records = Array(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00"),
      TestRecord("5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00"),
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00"),
      TestRecord("fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00")
    ).toList
    Console.withOut(outContent) {
      process(records, "2018-12-08")
    }
    val result = StringUtils.trim(outContent.toString).split("\\n").toList
    val required = "SAZuXPGUrfbcn5UA\n4sMM2LxV07bPJzwf\nfbcn5UAVanZf6UtG".split("\\n").toList
    println(result should contain theSameElementsAs required)
    //assert(StringUtils.trim(outContent.toString) == "SAZuXPGUrfbcn5UA\n4sMM2LxV07bPJzwf\nfbcn5UAVanZf6UtG","Output not matching")
  }

  test("test an inactive date should return empty"){
    val outContent = new ByteArrayOutputStream()
    val records = List(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00")
    )
    Console.withOut(outContent){
      process(records,"2018-08-02")
    }
    val result = StringUtils.trim(outContent.toString)
    val required = ""
    println(result should equal(required))
  }

  test("test an empty file") {
    val outContent = new ByteArrayOutputStream()
    val records :List[TestRecord]= List()
    Console.withOut(outContent){
      process(records,"2018-12-09")
    }
    val result = StringUtils.trim(outContent.toString)
    println(result should equal(""))
  }

  test("Invalid option testing should return Exception"){
    val args = Array("-f","cookie_log_2.csv")
    assertThrows[Exception] {
      LogTracker.main(args)
    }
  }

  test("invalid date format throws exception "){
    val args = Array("-f","cookie_log_2.csv","-d","2012-2034")
    assertThrows[Exception]{
      LogTracker.main(args)
    }
  }

  test("invalid empty file option should throw Exception"){
    val args = Array("-f","","-d","2012-09-09")
    assertThrows[Exception]{
      LogTracker.main(args)
    }
  }

  test("file not present should throw TrackerExecutionException"){
    val args= Array("-f","co.csv","-d","2019-08-09")
    assertThrows[TrackerExecutionException]{
      LogTracker.main(args)
    }
  }

  test("invalid timestamp in input file should throw TrackerExecutionException"){
    val outContent = new ByteArrayOutputStream()
    val records = List(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09")
    )
    assertThrows[TrackerExecutionException]{
      process(records,"2018-09-09")
    }
  }



  @throws(classOf[IOException])
  @throws(classOf[TrackerException])
  private def process (records:List[TestRecord], date:String): Unit ={
    val file: String = RecordGenerator.createFile(records)
    val args= Array("-f",file,"-d",date)
    LogTracker.main(args)
  }
}
