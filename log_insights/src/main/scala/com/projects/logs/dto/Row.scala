package com.projects.logs.dto

class Row(fieldIndex:Map[String,Int],fields:List[NamedField]){
  def getAs(idx:Int)= fields(idx).value
  def fieldToIndex(name:String):Int = fieldIndex(name)
}
object Row{
  def apply(fields: List[NamedField]): Row = {
    new Row(initFieldsIndex(fields), fields)
  }
  private def initFieldsIndex(fields:List[NamedField]): Map[String,Int] = {
    fields.zipWithIndex.map(r=>(r._1.name,r._2)).toMap
  }
}
