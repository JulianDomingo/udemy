package com.juliandomingo.spark

import org.apache._
import org.apache.spark.SparkContext
import org.apache.log4j._

object TotalSpent {
  def parseLines(line: String) = {
    val parameters = line.split(",")
    val customerID = parameters(0).toInt
    val amountSpent = parameters(2).toFloat
    (customerID, amountSpent)
  }
  
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sparkContext = new SparkContext("local[*]", "TotalSpent")
    
    val totalSpent = sparkContext.textFile("../customer-orders.csv")
                                 .map(parseLines)                    // <- Should be more descriptive.
                                 .reduceByKey( (x,y) => (x + y) )
                                 .map(x => (x._2, x._1) )
                                 .sortByKey()
                                 .collect()
    
    totalSpent.foreach(println) 
    
    // Better to sort RDD before printing, as sortByKey() is more optimized than ".sorted."
  }
  
}