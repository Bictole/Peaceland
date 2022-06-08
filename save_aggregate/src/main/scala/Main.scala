package saveaggregate

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

object Main extends App {
  def experiment() = {
    //val filePath = "s3a://arcatest0/test.json"
    //val filePath = "s3a://peaceland/addresses.csv"
    //val filePath = "/home/alexandrel/Peaceland/save_aggregate/test.json"

    val filePath = "/home/arcanix/school/spark/Peaceland/save_aggregate/test.json"

    // find these info on IAM
    val accessKey = "AKIAQTIILA2TLU7Y64XW"
    val secretKey = "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"
    
    val sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName("save_aggregate")
        .set("spark.driver.host", "127.0.0.1")

    val streamContext = new StreamingContext(sparkConf, Seconds(15))
    val sparkContext = streamContext.sparkContext
    val spark: SparkSession = SparkSession.builder.config(sparkContext.getConf).getOrCreate()

    import spark.implicits._ //cursed AF

    spark.sparkContext.setLogLevel("ERROR")

    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

    val df = spark.read.option("multiLine", true).json(filePath)
    df.show(false)

    putOnS3(df, "s3a://arcatest0/test.csv")
    val content = getFromS3(spark, filePath)
    content.show(5, false)

    val kafkaParams = Map(
        "bootstrap.servers" -> "localhost:9092",
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "group.id" -> "alert"
    )
    
    val topics = Array("peaceland")

    val stream = KafkaUtils.createDirectStream[String, String](
        streamContext,
        PreferConsistent,
        Subscribe[String,String](topics, kafkaParams)
    )

    println("start now")
    val encoderSchema = Encoders.product[SaveableEvent].schema
    stream.flatMap(record => {
        // Declare classes format to deserialize
        implicit val personFormat = Json.format[Person]
        implicit val coordsFormat = Json.format[Coords]
        implicit val eventFormat = Json.format[Event]
        val json = Json.parse(record.value())
        eventFormat.reads(json).asOpt
      })
      .map(event => {
        println(event)
        val serializedTime = event.timestamp.format(DateTimeFormatter.ISO_DATE_TIME)
        SaveableEvent(event.peacewatcherID, serializedTime, event.location, event.words, event.persons)
      })
      .map(event => List.fill(1)(event))
      .reduce((a, b) => a ++ b)
      //.map(eventList => Row(eventList:_*))
      //.map(event => {println(event); event})
      .map(
        eventRows => {
          //val rdd = sparkContext.makeRDD(eventRows)
          //val df = spark.createDataFrame(rdd, encoderSchema)
          val df = eventRows.toList.toDF
          val path = "s3a://arcatest0/archive_" //+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
          putOnS3(df, path)
        }
      )
      .print()

    streamContext.start()
    streamContext.awaitTermination()
  }

  def getFromS3(configuredSpark: SparkSession, inAddress: String) = {
    configuredSpark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .json("s3a://arcatest0/test.json")
  }

  def putOnS3(df: DataFrame, outAddress: String) = {
    println(outAddress)
    df.write.mode(SaveMode.Overwrite).parquet(outAddress)
  }
  
  experiment()
  //println("Hello, World!")
}