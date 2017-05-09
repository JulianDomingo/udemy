# Running Spark on a Cluster
* Using Amazon EMR 

#### Packaging and Deploying Application
* Ensure no paths to local filesystem in script (Use HDFS, S3, etc.)
* Package up Scala project into a JAR file (using export)
* Use spark-submit for testing locally instead of IDE.
  * spark-submit --class "package name.class object" "jar name" 

#### SBT
* Like Maven for Scala
* Manages library dependency tree
* Package up all dependencies into self-contained JAR
* Structure:
  * project
    * assembly.sbt file: "addSbtPlugin("com.eed3si9n" % "sbt-assembly"
      % "0.14.3")
  * src
    * main
      * scala file
  * build.sbt (Like a Maven pom.xml)
    * name := "Name"
    * version := "Version"
    * organization := "com.xxx"
    * scalaVersion := "Version"
    * libraryDependencies ++= Seq( dependencies )
* run "sbt assembly"
* Self-contained, so just do "spark-submit <jar name>"

#### Amazon EMR Clusters
* Simply use "val conf = new SparkConf()" for the cluster. EMR automatically knows how
  many nodes are needed, and the best way to set it up.
  * conf.setAppName("app name")
  * val sparkContext = new SparkContext(conf)
* Spark Driver (Software) -> Cluster Manager (i.e. Hadoop YARN) -> (Hardware) Cluster Worker(s) / Executors
* Spark-Submit Parameters:
  * --master (usually set automatically in AMZN EMR): yarn, hostname:port
    (standalone cluster), mesos://masternode:port 
  * --num-executors (must set explicitly with YARN, 2 by default): <number>
  * --executor-memory: Be aware of memory available to each executor (usually
    these are VM's.)
  * --totoal-executor-cores: <core number>
* Amazon EMR: quick way to create a cluster with Spark, Hadoop, and YARN
  pre-installed.
* **TIPS**: Use a subset of data for development purposes, and do it locally.
* Run on master node (SSH to provided address)
  * Connection problems: Security group -> Inbound -> Create SSH group
* EC2 Commands:
  * Copying files from S3 to EC2 instance: aws s3 cp s3://"orgname"/"jar name"
* **TERMINATING CLUSTER** : EMR Dashboard -> Terminate button
  * Billed for time by cluster uptime.

#### Partitioning RDD's
* Important when processing large data sets: need to think about *how* your
  data will be partitioned
  * I.e. in the movie pair script, self-join operations are expensive: spark
    won't distribute the joins on its own.
* rdd.partitionBy(): use on an RDD *before* running a large operation which
  **benefits** from partitioning.
  * I.e. join(), lookup(), cogroup(), groupWith(), reduceByKey(), groupByKey(),
    leftOuterJoin(), rightOuterJoin(), and combineByKey().
  * **These operations will preserve your partitioning in their result too**
    * Recall Spark separates job in stages determined by RDD actions:
      preserving the partitioning means less stages are needed to process the
data, and less stages means fewer shuffle operations == optimizing!
* Determining partition amount:
  * # of executors in cluster <= partitions (100 good starting point)
  * val newRdd = rdd.partitionBy(new HashPartitioner(100))
  * newRdd.groupByKey() <- partitioned RDD operation

#### Best practices for running on a Cluster
* All settings are overwritten from command line if Scala script has settings
  hardcoded in SparkConf instantiation. **Just use an empty, default SparkConf object.**
  * Pass in specific parameters in terminal when invoking the spark-submit
    command.
* Get data and scripts someplace where EMR can easily access them.
  * Set file permissions to be accessible (chmod), and set file paths to be
    "s3n://..."

#### Troubleshooting & Managing Dependencies
* Access UI for logging: https://localhost:4040
* Bundle Java / Scala packages outside of what's pre-loaded in EMR into JAR
  file. 
  * **NOTE:** include only what you absolutely need: time is wasted



 
