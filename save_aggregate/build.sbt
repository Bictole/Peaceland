scalaVersion := "2.13.8"

name := "save_aggregate"
organization := "peaceland"
version := "1.0"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.8.0",
    "org.apache.kafka" % "kafka-clients" % "2.8.0",
    "org.apache.spark" %% "spark-core" % "3.2.1",
    "org.apache.spark" %% "spark-sql" % "3.2.1",
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.1",
    "org.apache.spark" %% "spark-streaming" % "3.2.1",
    "org.apache.hadoop" % "hadoop-common" % "3.1.2",
    "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.1.2",
    "org.apache.hadoop" % "hadoop-aws" % "3.1.2",
    "org.apache.hadoop" % "hadoop-client" % "3.1.2",
)