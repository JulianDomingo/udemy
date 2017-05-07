# Introduction to Spark

* Engine / framework for large-scale data processing.
* Lets you import data from a distributed data store, and lets you efficiently
  and simply process that data.
* On a cluster.
* High-level architecture:
  * Driver Program (Spark context) -> Cluster Manager (i.e. Spark, YARN) ->
    {Executor(s) (Cache, Tasks)}
* Faster than Hadoop MapReduce in memory / disk.
* DAG (Topological) Engine: optimizes workflows.
* Built around one main concept: Resilient Distributed Dataset (RDD)
  * Abstraction object over a large set of data.

#### Components
* Spark Streaming, Spark SQL, MLLib, GraphX <- Spark Core

#### Benefits of using Scala with Spark
* Spark written in Scala
* Functional programming model good fit for distributed processing
* Optimizes efficiency
 
#### Resilient Distributed Dataset (RDD)
* Abstracts complexity of fault-tolerance and distributed nature of processing
  data.
  * Resilient <=> Fault-tolerant
  * Distributed
  * Dataset - RDD object is a giant set of data.
* Spark context object always defined: creates an RDD for you.

#### Creating RDD's
* sc.textFile(), hiveCtx.sql(query), parallelize(list)

#### Transforming RDD's
* map: applies function to an RDD.
  * val rdd = sc.parallelize(list)
  * val squares = rdd.map(x => x * x)
* flatmap: map() where output is not a one-to-one relationship. 
* filter
* distinct
* sample: returns RDD of random elements from existing RDD. Useful for testing. 
* union, intersection, subtract, cartesian: self-explanatory

#### Why Functional Programming?
* Advantageous when you have to perform lots of different operations on fixed
  data / data with known amount of variation.

#### RDD Actions
* Lazy evaluation: **Nothing in Spark driver program actually happens until you call one of these RDD actions!**
  * Spark will go back and figure out what's the optimal path to execute the
    data processing, creating a DAG with optimized workflow.
* Collect: gathers results of RDD transformations
* CountByValue
* Take / top: takes the first x rows of RDD 
* Reduce: combines values w.r.t. keys through some function.
 
#### Scala Program Steps
* Import needed libraries
* Set up spark context object
  * new SparkContext("local[\*]", "<class name>")
  * "local[\*]" : run Spark job on local machine, and utilize all of its CPU's
    to distribute workload.
* Load data
* Extract the data we care about
* Perform action on the new RDD with (extracted) data

### Spark Internals
* Execution plan is created from your RDD's (DAG)
* Shuffle operations: expensive in cluster, need to optimize your Spark job so
  least number of shuffle operations are necessary. 
* Job broken into stages based on when data needs to be reorganized (shuffle
  operations)
* Each stage broken into tasks, or individual nodes (which may be distributed across the cluster)
* Tasks scheduled across  your cluster and executed.

#### Key / Value RDD's
* RDD holds data as key-value pairs.
  * rdd.map(x => (x, value))
* reduceByKey(): combines values with same key
  * rdd.reduceByKey( (x,y) => x + y) adds up values of the same key, where
    x and y are values.
* groupByKey(): group values with same key
* sortByKey(): sort RDD by keys
* keys(), values(): returns RDD of just keys / values
* join, rightOuterJoin, leftOuterJoin, cogroup, subtractByKey
* More efficient to use **mapValues()** and **flatMapValues()** if transformation of RDD's doesn't affect keys in any way.

* Ex. - Average number of friends by age:
  * def parseLine(line: String) = { val age = line.split(",")(2).toInt
                                    val numFriends = line.split(",")(3).toInt
                                    (age, numFriends) // return tuple
                                  }
  * val rdd = sc.textFile(data).map(parseLine)
  * val averageThis = rdd.mapValues(x => (x,1)).reduceByKey( (x,y) => (x.\_1
    + y.\_1, x.\_2 + y.\_2))
    * (x => (x, 1)): (number of friends => (number of friends, 1))
    * reduceByKey operation: ((numFriends1, 1), (numFriends2, 1)) =>
      (numFriends1 + numFriends2, 1 + 1)
  * val averagesByAge = averageThis.mapValues(x => (x.\_1  x.\_2))
  * **Where the spark job starts processing**:
    * val results = averagesByAge.collect()
    * results.sorted.foreach(println)
  * Result: (age, (combined friend count, people of age "age"))

#### Filtering RDD's
* Saving Spark from doing unnecessary processing.
* Simply pass a function which returns a boolean:
  * True == keep 
  * False == filter out
  * rdd.filter(x => equality of kept value(s))
* Before invoking RDD action function, always best to remove any feature
  columns which are no longer need.
  * Calculate Min Temp: 
  * Before (need identifier in initial filter()): (<weather station>, <min temp identifier>, <temperature>)
  * After (no longer need identifier): 
    * rdd.filter(x => (x._1, x._3.toFloat)) => (<weather station>, <temperature>)
    * Reduces number of shuffle operations needed for cluster to complete job.

#### Map vs. Flatmap
* map(): One-to-one mapping of RDD to transform and transformed RDD
  * By line
* flatMap(): One-to-many mapping
  * Create many new elements from each element
  * **Every element in return value of flatMap() is a new row in the resultant
    RDD.**
* countByValue() returns Scala map. For a return value of RDD:
  * rdd.map(x => (x, 1)).reduceByKey( (x,y) => x + y) 
* Flipping key / value pairs: rdd.map( (x => (x.\_2, x.\_1)) )
* Sorting: sortByKey() 

 

 
