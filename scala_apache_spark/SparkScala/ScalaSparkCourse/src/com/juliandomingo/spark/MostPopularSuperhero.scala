package com.juliandomingo.spark

import org.apache._
import org.apache.spark.SparkContext
import org.apache.log4j._
import scala.io.Source

object MostPopularSuperhero {
  def getOccurrences(line: String) = {
    val superheros = line.split(" ")
    (superheros(0), superheros.length - 1)
  }
  
  def identify(line: String): Option[(Int, String)] = {
    val mappingOfID = line.split("\"")
    if (mappingOfID.length > 1) {
      return Some(mappingOfID(0).trim().toInt, mappingOfID(1))
    }
    else {
      return None
    }
  }

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "MostPopularSuperhero")
    
    val relations = sc.textFile("../Marvel-graph.txt")
    val occurrences = relations.map(getOccurrences)
    
    val names = sc.textFile("../Marvel-names.txt")
    val identifications = names.flatMap(identify)
    
    val result = occurrences.reduceByKey( (x, y) => (x + y) )
                            .map(x => (x._2, x._1) )
                            .max()
                            
    val appearsTheMost = identifications.lookup(result._2.toInt)(0)
    val appearances = result._1
    
    println(s"$appearsTheMost appears the most in Marvel movies, with a total of $appearances appearances.")  
  }
}