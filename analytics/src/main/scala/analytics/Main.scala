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
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SQLContext


object Main {
    def main(args: Array[String]): Unit = {
        val accessKey = "AKIAS5I4RNJFMB52MQW5"
        val secretKey = "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"

        val sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName("analytics")
        .set("spark.driver.host", "127.0.0.1")

        val sparkContext = new SparkContext(sparkConf)
        val spark: SparkSession = SparkSession.builder.config(sparkContext.getConf).getOrCreate()

        spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
        spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
        spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

        val filePath = "s3a://peaceland-avem/event"
        val fileExtension = "obj"

        val obj = sparkContext.objectFile(filePath + "-*." + fileExtension).map((obj : SaveableEvent) =>
            Event(
            obj.peacewatcherID,
            LocalDateTime.parse(obj.timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            obj.location,
            obj.words,
            obj.persons
            ))
        val weekDays = obj.groupBy(x => x.timestamp.getDayOfWeek())
		println(s"Days of the week with the most pissed off people:")
		weekDays.foreach(x => println(s"${x._1} : ${x._2.size}"))
	}
}