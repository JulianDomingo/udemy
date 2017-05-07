package com.juliandomingo.spark

import org.apache._
import org.apache.spark.SparkContext
import org.apache.log4j._

object WordCountFiltered {
  var blackList: Array[String] = Array("you", "to", "your", "the", "a", "of", "and", "that", "it", "in", "is", "for", "on")
  
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sc = new SparkContext("local", "WordCountFiltered")
    
    val filtered = sc.textFile("../book.txt")
                     .flatMap(x => x.split("\\W+") )
                     .map(x => x.toLowerCase() )
                     .map(x => (x, 1) )
                     .reduceByKey( (x, y) => (x + y) )
                     .map(x => (x._2, x._1) )
                     .sortByKey()
                     .filter(x => !blackList.contains(x._2) )
    
    for (tuple <- filtered) {
      val word = tuple._1
      val count = tuple._2
      println(s"$word : $count")
    }  
  }
}