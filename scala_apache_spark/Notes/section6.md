# Spark SQL - Dataframes & DataSets
* A more structured RDD comprised of row objects.
  * Can run SQL queries on DataFrames
  * Has schema (more effective storage)
  * R/W to JSON, Hive, Parquet
  * Comm with JDBC/ODBC, Tableau

* DataFrame vs DataSet
  * DataSet explicitly wrap a given type/struct (knows type at compile time)
  * DataFrame is a DataSet of row objects
  * DataFrame schema inferred at runtime vs. DataSet schema inferred at compile
    time
  * Convert RDD's to DataSets: rdd.toDS()

* Trend: use RDD's less, DataSets more
  * **More efficient**: efficient serialization, optimal execution paths determined
    at compile time (DAG workflow of Spark job)
  * **Interoperability**: Spark MLLib / Spark Streaming moving towards using
    DataSets for primary API
  * **Simplify Development**: perform most SQL operations in one line

* Implementation Differences
  * Use a SparkSession object as opposed to a SparkContext (SparkSession wraps
    a SparkContext and SQLContext).
    * Stop session when done.

* Dataframe commands:
  * show(number): shows the first "number" number of row objects.
  * select("field name"): like a SQL select statement
  * filter(myResultDataFrame("field name") <boolean condition>)
    * Run RDD actions on DataFrame objects w.r.t. fields of DataFrame
  * dataFrame.rdd().map(func)

* Treat as a Database (exposes JDBC/ODBC server if Spark was built with Hive
  support)
  * Open up a connection (cache results of DataFrame, open up server to access
    results)
    * SQL shell to Spark SQL, essentially
      * Query tables, create new tables

* User-defined Functions:
  * org.apache.spark.sql.functions.udf
    * val square = (x => x * x)
    * squaredDF = df.withColumn("column name", square(value) )

* Defining a SparkSession:
  val sparkSession = SparkSession
                     .builder
                     .appName("app name")
                     .master("local[*]") <- when deploying, this is omitted
                     .getOrCreate()

* Mapping unstructured data into DataSets:
  * case class <Class Name>(parameters) <- parameters become column fields in
    the DataSet
    * def mapper(line: String): Person = {
        return Person(tokenized line)
      }
    * val data = sparkSession.sparkContext.textFile(path)
    * val people = data.map(mapper)
  * **Must infer schema:** import spark.implicits.\_
  * val schemaPeople = people.toDS 

* Debugging / Viewing: dataset.printSchema()

* Create SQL table from Dataset: dataset.createOrReplaceTempView(table name)
  * val dataFrame = sparkSession.sql(sql command)
    * can run SQL commands on Dataframes registered as tables
      (createOrReplaceTempView("exampleTable") -> can run sparkSession.sql() on
the "exampleTable" table.
  * dataFrame.collect() -> returns array of results from SQL command

* **Don't forget to end SparkSession!** : sparkSession.stop()

* dataSet.cache(): use when performing more than one action on a data set
  (similar to broadcast(), or partitionBy() for cluster management).

* Use sparkSQL's SQL-like commands instead of plain SQL for better
  optimization:
  * dataSet.cache()
  * dataSet.select("column name").show() -> dataSet.filter(dataSet("column
    name") <conditional filter>).show()

