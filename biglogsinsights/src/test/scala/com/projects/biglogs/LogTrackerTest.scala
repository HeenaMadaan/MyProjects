package com.projects.biglogs

import java.io.{ByteArrayOutputStream, FilePermission}
import java.nio.file.Files

import com.projects.biglogs.config.AppConfig
import com.projects.biglogs.util.{ConfigParser, RecordGenerator, SparkBuilder, TestRecord}
import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should._


class LogTrackerTest extends AnyFunSuite with Matchers with BeforeAndAfter with BeforeAndAfterAll {
   var inputPath:String  = _
  var batchId:Long=_
  val spark = SparkBuilder.spark
  val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
  val basePath = "/home/milap/Documents/Projects/biglogsinsights/src/test/resources"
  var activityConfig:AppConfig = null
/*  override def beforeAll(){
    val workingDir = fs.getWorkingDirectory
    val basePath = s"$workingDir/src/test"
    tempPath = new Path(s"$basePath/input")
  }*/

  before{
    batchId = System.currentTimeMillis()
    inputPath = s"$basePath/input/$batchId"
    activityConfig = AppConfig(inputPath,s"$basePath/output")
  }
/*
   test("Should return one most active cookie") {
     val records = List(
       TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
       TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00"),
       TestRecord("5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00"),
       TestRecord("AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00"),
       TestRecord("SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00"),
       TestRecord("4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00"),
       TestRecord("fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"),
       TestRecord("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00")
     )
       process(records, "2018-12-09", batchId)

     val resultDF = spark.read.format("csv").option("header","true").load(s"$basePath/output/batch$batchId")
     // extracting column to list of string. asInstanceOf
     val result = resultDF.select("cookie").collect().map(r => r(0).asInstanceOf[String]).toList
     val output = List("AtY0laUfhglK3lC7")
     println("test result 1",result should contain theSameElementsAs output)
   }

  test("Should return multiple active cookies") {
    val records = List(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00"),
      TestRecord("5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00"),
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00"),
      TestRecord("fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"),
      TestRecord("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00")
    )
    process(records, "2018-12-08", batchId)

    val resultDF = spark.read.format("csv").option("header","true").load(s"$basePath/output/batch$batchId")
    // extracting column to list of string. asInstanceOf
    val result = resultDF.select("cookie").collect().map(r => r(0).asInstanceOf[String]).toList
    val output = List("4sMM2LxV07bPJzwf","SAZuXPGUrfbcn5UA","fbcn5UAVanZf6UtG")
    println("test result 2",result should contain theSameElementsAs output)
  }
*/

  test("test an inactive date should return empty"){
    val records = List(
      TestRecord("AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00"),
      TestRecord("SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00")
    )
    process(records,"2018-08-02",batchId)
    val resultDF = spark.read.format("csv").option("header","true").load(s"$basePath/output/batch$batchId")
    val result = resultDF.collect().map(r=> Option(r(0)).getOrElse("").asInstanceOf[String]).toList
    val required = List("")
    println("test result 3",result should equal(required))
  }

  def process (records:List[TestRecord], date:String, batchId:Long) = {
    val path = RecordGenerator.createFile(records, inputPath, spark)
/*    val args = Array("-p",path,"-d",date)
    LogTracker.main(args)*/
    LogTracker.analyze(spark,activityConfig,date, batchId)
  }

  override def afterAll(): Unit ={
    try {
      fs.delete(new Path(basePath),true)
    }
  }
/*  after{
    try {
      fs.delete(new Path(basePath),true)
    }
  }*/
}
