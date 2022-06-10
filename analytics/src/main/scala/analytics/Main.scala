import play.api.libs.json._

import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter 

import scala.io.Source

import org.apache.spark.sql.SparkSession

import org.apache.spark.{SparkContext, SparkConf}

import org.apache.log4j.{Level, Logger}


object Main {
    def main(args: Array[String]): Unit = {
        // keep only the errors
        Logger.getLogger("org").setLevel(Level.ERROR)

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
        println("\n\n")
        val weekDays = obj.groupBy(x => x.timestamp.getDayOfWeek())
        println(s"Days of the week with the most pissed off people:")
        weekDays.foreach(x => println(s"${x._1} : ${x._2.size}"))
    }
}
