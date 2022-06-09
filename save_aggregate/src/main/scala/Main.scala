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
import org.apache.spark.sql.SQLContext  

//import scala.collection.JavaConversions._
import scala.reflect.ClassTag

object Main extends App {
  def experiment() = {
    val accessKey = "AKIAQTIILA2TLU7Y64XW"
    val secretKey = "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"
    
    val sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName("save_aggregate")
        .set("spark.driver.host", "127.0.0.1")

    val streamContext = new StreamingContext(sparkConf, Seconds(15))
    val sparkContext = streamContext.sparkContext
    val spark: SparkSession = SparkSession.builder.config(sparkContext.getConf).getOrCreate()
    val sqlContext = new org.apache.spark.sql.SQLContext(sparkContext)
    //import sqlContext.implicits._

    spark.sparkContext.setLogLevel("ERROR")

    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

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

    import spark.implicits._

    val list = Seq(
      SaveableEvent(0, "", Coords(1.2, 1.2), List("foo", "bar"), List(Person("lmao", 3.6))),
      SaveableEvent(0, "", Coords(1.2, 1.2), List("foo", "bar"), List(Person("lmao", 3.6))),
      SaveableEvent(0, "", Coords(1.2, 1.2), List("foo", "bar"), List(Person("lmao", 3.6))),
      SaveableEvent(0, "", Coords(1.2, 1.2), List("foo", "bar"), List(Person("lmao", 3.6))),
      SaveableEvent(0, "", Coords(1.2, 1.2), List("foo", "bar"), List(Person("lmao", 3.6)))
    )

    val df = list.toDF
    df.show()


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
      }).saveAsTextFiles("s3a://arcatest0/testingsaveastext", ".txt")
      //.saveAsObjectFile("s3a://arcatest0/testingsaveasobj", ".obj")
/*/*      .map(event => {
        val values = event.productIterator.toSeq.toArray
        val encoderSchema = Encoders.product[SaveableEvent].schemaq
        import org.apache.spark.sql.Row
        import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
        val row: Row = new GenericRowWithSchema(values, encoderSchema)
        row
      })*/
      .map(event => List.fill(1)(event))
      .reduce((a, b) => a ++ b)
      .map(event => {println(event); event})
      .map(
        eventRows => {
          val encoderSchema = Encoders.product[SaveableEvent].schema
          println(eventRows.getClass)
          //val df = eventRows.toDF
          //df.show()
          val rdd = spark.sparkContext.parallelize(eventRows)
          val df = sqlContext.createDataFrame(rdd)
          df.show()
          df
/*          val encoderSchema = Encoders.product[SaveableEvent].schema
          //val eventList = eventRows
          //val df = rdd.toDF()
/*          val df = spark.createDataFrame(
            spark.sparkContext.parallelize(eventList),
            encoderSchema
          )*/
          //val s = eventRows.toSeq
          //val df = s.toDF
          val rdd = sparkContext.parallelize(eventRows)
          //val rowRdd = rdd.map(v => Row(v: _*))
          val df = spark.createDataFrame(eventRows, encoderSchema)
          df.show(1, false)
          //val df = eventList.toDF // nullptr exception here
          val path = "s3a://arcatest0/archive"
          putOnS3(df, path)*/
        }
      )*/
      //.print()

    streamContext.start()
    streamContext.awaitTermination()
  }

  def getFromS3(configuredSpark: SparkSession, inAddress: String) = {
    configuredSpark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .json(inAddress)
  }

  def putOnS3(df: DataFrame, outAddress: String) = {
    println(outAddress)
    df.write.mode(SaveMode.Overwrite).parquet(outAddress)
  }
  
  experiment()
}