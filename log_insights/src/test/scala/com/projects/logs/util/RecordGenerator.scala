package com.projects.logs.util

import java.io.{BufferedWriter, File, FileWriter, IOException, PrintWriter}


object RecordGenerator{

  @throws(classOf[IOException])
    def createFile(testRecords: List[TestRecord])={
    val file= File.createTempFile("input-"+System.currentTimeMillis(),".csv")
    val out = new BufferedWriter(new FileWriter(file.getPath))
    val writer = new PrintWriter(out)
    val data = content(testRecords)
    try {
      writer.write(data)
    } finally {
      writer.close()
    }
    file.getPath
  }
  private def content(records:List[TestRecord]) = {
    val sb= new StringBuilder
    sb.append("cookie,timestamp\n")
    records.foreach(tr => sb.append(tr.cookie+","+tr.timestamp+"\n"))
    sb.toString
  }
}
