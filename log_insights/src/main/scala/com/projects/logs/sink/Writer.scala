package com.projects.logs.sink

abstract class Writer[T]{
  def write(rows:List[T])
}
