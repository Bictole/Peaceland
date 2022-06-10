ThisBuild / scalaVersion := "2.13.8"

name := "peaceland"
organization := "peacecorp"
version := "1.0"

lazy val data = (project in file("data"))
    .settings(
        name := "data",
        libraryDependencies ++= commonDependencies ++ Seq(
        )
    )

lazy val peacewatcher_simulator = (project in file("peacewatcher_simulator"))
    .settings(
        name := "peacewatcher_simulator",
        libraryDependencies ++= commonDependencies ++ Seq(
            dependencies.kafkaClients,
            dependencies.log4jAPI
        )
    )
    .dependsOn(
        data
    )

lazy val archives_store = (project in file("archives_store"))
    .settings(
        name := "archives_store",
        libraryDependencies ++= commonDependencies ++ Seq(
            dependencies.kafkaClients,
            dependencies.sparkCore,
            dependencies.sparkSQL,
            dependencies.sparkStreamKafka,
            dependencies.sparkStream,
            dependencies.hadoopCommon,
            dependencies.hadoopMapReduce,
            dependencies.hadoopAWS,
            dependencies.hadoopClient
        )
    )
    .dependsOn(
        data
    )

lazy val alert_consumer = (project in file("alert_consumer"))
    .settings(
        name := "alert_consumer",
        libraryDependencies ++= commonDependencies ++ Seq(
            dependencies.kafkaClients,
            dependencies.sparkCore,
            dependencies.sparkSQL,
            dependencies.sparkStreamKafka,
            dependencies.sparkStream
        )
    )
    .dependsOn(
        data
    )

lazy val analytics = (project in file("analytics"))
    .settings(
        name := "analytics",
        libraryDependencies ++= commonDependencies ++ Seq(
            dependencies.sparkCore,
            dependencies.sparkSQL,
            dependencies.hadoopCommon,
            dependencies.hadoopMapReduce,
            dependencies.hadoopAWS,
            dependencies.hadoopClient
        )
    )
    .dependsOn(
        data
    )

// DEPENDENCIES

lazy val dependencies =
    new {
        val pJson = "com.typesafe.play" %% "play-json" % "2.8.0"
        val log4jAPI = "org.apache.logging.log4j" % "log4j-1.2-api" % "2.17.2"
        val log4jCore = "org.apache.logging.log4j" % "log4j-core" % "2.17.2"
        val log4jslf = "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.17.0"
        val kafkaClients = "org.apache.kafka" % "kafka-clients" % "2.8.0"
        val sparkCore = "org.apache.spark" %% "spark-core" % "3.2.1"
        val sparkSQL = "org.apache.spark" %% "spark-sql" % "3.2.1"
        val hadoopCommon = "org.apache.hadoop" % "hadoop-common" % "3.1.2"
        val hadoopMapReduce = "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.1.2"
        val hadoopAWS = "org.apache.hadoop" % "hadoop-aws" % "3.1.2"
        val hadoopClient = "org.apache.hadoop" % "hadoop-client" % "3.1.2"
        val sparkStreamKafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.1"
        val sparkStream = "org.apache.spark" %% "spark-streaming" % "3.2.1"
    }

lazy val commonDependencies = Seq(
    dependencies.pJson,
    dependencies.log4jslf
)
