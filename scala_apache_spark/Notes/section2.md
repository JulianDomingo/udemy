# Learning Scala

* Built for Spark
* For distributed processing clusters
* Using Scala provides fatest, reliable Spark jobs.
* Runs on top of JVM, compiles to Java bytecode (access to all of Java)
* Functional programming

* Variables (var) vs Values (val)
  * val: immutable constant: "val x: String = "x""
  * var: mutables

* Printing: println(<value>)
  * No need to toString() for printing.
  * printf style: println(f"$numberOne%05d") == "00001"
  * Subbing values: println(s"Hello, $name") == "Hello, Julian"
  * Subbing expressions: println(s"One plus two is ${1 + 2") == "One plus two
    is 3"

* Syntax:
  * Declares variable name before type. 
  * <var|val> <name>: <type> = <assignment>

* Use immutable objects (val's) as often as possible, to avoid unintended errors formed
  from functions inexplicitly changing mutables.
  * Scala prevents reassignment of val's.

* Can infer types if no ambiguity in assignment.

* Types: same as Java wording, but all start with capital letters: "Boolean,"
  "Int," etc.

* Regex
  * val regex = """<reg expression>""".r
  * val regex(storing object) = input string
  * I.e. if regex was for finding numbers, can do regex.toInt
 
* String comparison: "=="

* Switch statements:
  * <val> match {
      case 1 => <appropriate handling>
      case _ => <default handling>
    } 

* For / While / Do-While loops:
  * "<-" : range operator
  * I.e. for (index <- 1 to 4) { ... }

* Expressions:
  * {<instantiation>; <change>} 
  * I.e.: {val x : Int = 10; x + 10} == 20

* Functions:
  * def <function name>(<var name>: <type>) : <return type> = { ... }
  * No need to return anything! Last expression computed is the return value.
    * def cubeIt(x: Int) : Int = { x * x * x }
    * cubeIt(2) = 8

* Passing functions to other functions:
  * def transformIt(x: Int, function: <input type> => <output type>) : Int
    = { function(x) }
  * Modular: transformIt(2, <cubeIt | squareIt | ...>)

* Inline passing of functions (Lambdas):
  * transformIt(3, x => x * x) == squareIt(3)
  * transformIt(3, x => {val y = x * 2; y * y}) == squareIt(3 * 2)

* Data Structures:
  * Tuples: immutable lists, often thought of as database fields, or columns.
    Represent some common entity.

    * **1-indexed based Tuples in Scala.**
    * val tuple = (1,2,3)

    * Indexing: <structure>._<index number>
    * tuple._1 == first element, 1
    * Can use "->": val tuple = "1" -> "One"
      * tuple._1 == 1, tuple._2 == "One"
    * Not limited to same type objects in a tuple.

  * List: immutable linked lists under the hood.
    * val list = List(1,2,3)
    * list(0) == 1 == list.head
    * list.tail = 2,3 (remaining elements excluding head)

    * Iterating through list:
      * for (ship <- shipList) 
    
    * Lambdas (Map / Reduce / Filter):
      * val reversedShipNames = list.map( (ship: String) => {ship.reverse})
      * val sum = numberList.reduce( (x: Int, y: Int) => {x + y})
      * val noFives = numberList.filter( (contender: Int) => contender != 5)
      * For only one parameter: filter(_ != 5)
      * Difference is these don't operate on a cluster.

    * Concatenation: list1 ++ list2
    * Sorting: list1.sorted
    * Unique: list1.distinct (removes duplicates)
    * Min / Max / Contains: list.max / list.min, list.contains(value)
    * Sum: list.sum

  * Maps (Lookup Tables):
    * val map = Map(1 -> "One", 2 -> "Two")
    * map(1) == "One"
    
    * Contains: map.contains(key)
    * Exceptions: util.Try(map(keyToTry)) getOrElse "Key unknown."

