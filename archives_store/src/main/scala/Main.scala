package archives_store

import play.api.libs.json._

import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter 

import scala.io.Source

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig

import org.apache.spark.sql.{SparkSession, DataFrame, SaveMode, Row, SQLContext}

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SQLContext  

import scala.reflect.ClassTag

import org.apache.log4j.{Level, Logger}

import data._

object Main extends App {
  // keep only the errors
  Logger.getLogger("org").setLevel(Level.ERROR)

  //access and secret key for AWS S3, found on IAM
  val accessKey = ""
  val secretKey = ""
  
  val sparkConf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("save_aggregate")
      .set("spark.driver.host", "127.0.0.1")

  //here Seconds(15) means that we fetch the batch every 15 secs, if we were to put it to 1 day, we'd aggregate daily
  val streamContext = new StreamingContext(sparkConf, Seconds(15))
  val sparkContext = streamContext.sparkContext
  val spark: SparkSession = SparkSession.builder.config(sparkContext.getConf).getOrCreate()
  val sqlContext = new org.apache.spark.sql.SQLContext(sparkContext)

  //I'm keeping this here as a reminder of how glad I am that we're no longer using it:
  //import sqlContext.implicits._


  spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
  spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
  spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

  val kafkaParams = Map(
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "aggregate"
  )
  
  val topics = Array("peaceland")

  val stream = KafkaUtils.createDirectStream[String, String](
      streamContext,
      PreferConsistent,
      Subscribe[String,String](topics, kafkaParams)
  )

  //path to file that we will read from and write to
  val filePath = "s3a://peaceland-avem/event"
  val fileExtension = "obj"

  //this is an example of how to read. it won't work cause I reached my max cap on S3.
  //val obj = sparkContext.objectFile(filePath + "-*" + fileExtension)
  //obj.foreach(println)

  stream.flatMap(record => { //deserializes records into instances of Event case class
      val json = Json.parse(record.value())
      Event.EventFormatter.reads(json).asOpt
    })
    .map(event => { //print the received event, and convert the Event cas class to a Saveable Event to avoid timestamp issues
      println(event)
      val serializedTime = event.timestamp.format(DateTimeFormatter.ISO_DATE_TIME)
      SaveableEvent(event.peacewatcher_id, serializedTime, event.location, event.words, event.persons, event.battery, event.temperature)
    })
    .saveAsObjectFiles(filePath, fileExtension) //save the batch as an object file that will contain a map partition of all elements of the batch
  
  streamContext.start()
  streamContext.awaitTermination()
}