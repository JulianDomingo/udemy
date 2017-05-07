package com.juliandomingo.spark


import org.apache.spark._
import org.apache.log4j._
import org.apache.spark.SparkContext._
import java.text.SimpleDateFormat

import scala.math.max

object HighestPrecipitationDay {
  def parseLine(line: String) = {
    val parameters = line.split(",")
    
    val day = parameters(1)
    val info = parameters(2)
    val precipitation = parameters(3).toInt
    
    (day, info, precipitation)
  }
  
  def main(args: Array[String]) = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sparkContext = new SparkContext("local[*]", "HighestPrecipitation")
    val data = sparkContext.textFile("../1800.csv")
    
    val lines = data.map(parseLine)
       
    val parsedLines = lines.filter(x => (x._2 == "PRCP"))
    
    val filtered = parsedLines.map(x => (x._1, x._3.toFloat))
       
    val reduced = filtered.reduceByKey( (x,y) => (max(x,y)) )
    
    val results = reduced.collect()
    
    val highestDay = results.max._1
    val highestPrecipitation = results.max._2

    println(s"The day $highestDay experienced the most precipitation of $highestPrecipitation.")
  }
}