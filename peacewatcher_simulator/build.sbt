scalaVersion := "2.13.8"

name := "peacewatcher_simulator"
organization := "peaceland"
version := "1.0"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.8.0",
    "org.apache.kafka" % "kafka-clients" % "2.8.0")
        
