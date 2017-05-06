
package com.juliandomingo.spark

import org.apache.spark._

import org.apache.spark.SparkContext._
import org.apache.log4j._

object FriendsByFirstName {
  def parseLine(line: String) = {
    val parameters = line.split(",")
    val firstName = parameters(1)
    val age = parameters(3).toInt
    (firstName, age)
  }
  
  def main(args: Array[String]) {
    // Set logging to error level.
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sc = new SparkContext("local[*]", "FriendsByFirstName")
    val data = sc.textFile("../fakefriends.csv")
    val rdd = data.map(parseLine)
    
    val uniqueNames = rdd.mapValues(x => (x, 1)).reduceByKey( (x,y) => (x._1 + y._1, x._2 + y._2))
    val results = uniqueNames.mapValues(x => (x._1 / x._2)).collect()
    
    results.sorted.foreach(println)
  }
}