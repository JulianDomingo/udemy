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




