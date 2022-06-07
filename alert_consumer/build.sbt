scalaVersion := "2.13.8"

name := "alert_consumer"
organization := "peaceland"
version := "1.0"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.8.0",
    "org.apache.kafka" % "kafka-clients" % "2.8.0",
    "org.apache.spark" %% "spark-core" % "3.2.1",
    "org.apache.spark" %% "spark-sql" % "3.2.1",
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.1",
    "org.apache.spark" %% "spark-streaming" % "3.2.1" 
)