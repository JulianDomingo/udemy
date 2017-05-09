# Advanced Examples of Spark Programs

#### Merging Data: Broadcast Variables
* Broadcast Variable: Taking a chunk of data and explicitly sending it to all
  nodes in the cluster (like MPI)
* sparkContext.broadcast(): ship off whatever you want
* sparkContext.value() to get the object back.
  * Efficiency: transmits the data through the network only once. 
* Handling weird characters:
  * implicit val codec = Codec("UTF-8")
  * codec.onMalformedInput(CodingErrorAction.REPLACE)
  * codec.onUnMappableCharacter(CodingErrorAction.REPLACE)
* Grabbing other file types (not .txt or .csv):
  * Source.fromFile(...).getLines()
* Adding to a Map object:
  * var map: Map[keyType, valueType] = Map()
  * map += (key -> value)

* Broadcast ex:
  * sparkContext.broadCast(loadMap)
    * loadMap() returns a map -> available to all nodes in cluster
  * **Key:** avoid transmitting large datasets more than once (if possible).
* **Takeaway** : information should be available to the entire cluster when
  needed (i.e. when distributed mapping is needed).

* Option objects:
  * Contains a "Some" value (exists), or a "None" value (doesn't exist
    / doesn't fit protocol)
  * Error Handling
    * Use flatMap(function), and function should return a list of
      Option[transformed rows].

#### Degrees of Separation (BFS)
* Accumulator: allows many executors to increment a shared variable
  * LongAccumulator("name of accumulator"): initialized to 0.
  * var hitCounter: Optino[Accumulator[Int]] = None
    * hitCounter = Some(sc.accumulator(0))
    * hitCounter.isDefined
    * hitCounter.get.add(1)

#### Item-based Collaborative Filtering
* Defining relations among items in a data set
* **Caching RDD's:** 
  * Prevents recreating an RDD from scratch if > 1 RDD action is needed for
    a particular RDD.
  * rdd.cache() -> to memory
  * rdd.persist() -> to disk and/or memory
  * rdd.rddaction().cache()
* **Self-join**:
  * rdd.join(rdd)
    * Generates an RDD containing a permutation of all values.
    * Will most likely need to filter duplicates (same value, different
      positions)
* Defining own types:
  * type <var name>: <type name>
* Similarities: compute cosine
* Reverse sorting: sortByKey(false)
* Taking portion of RDD: take(sample amount)

