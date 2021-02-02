package com.projects.logs.sink

case class ConsoleWriter[T]() extends Writer[T]{
  override def write(rows: List[T]): Unit = rows.foreach(println)
}
