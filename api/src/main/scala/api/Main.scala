package api

import java.util.Properties
import play.api.libs.json._
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

object Main{

    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf()
            .setAppName("peaceland_alert")
            .setMaster("local[*]")
            .set("spark.driver.host", "127.0.0.1")

        val ssc = new StreamingContext(sparkConf, Seconds(5))

        val kafkaParams = Map(
            "bootstrap.servers" -> "localhost:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "alert"
        )
        
        val topics = Array("alert")

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String,String](topics, kafkaParams)
        )

        stream.flatMap(record => {
            // Declare classes format to deserialize
            implicit val personFormat = Json.format[Person]
            implicit val coordsFormat = Json.format[Coords]
            implicit val eventFormat = Json.format[Event]
            val json = Json.parse(record.value())
            eventFormat.reads(json).asOpt
        }).print()
        
        ssc.start()
        ssc.awaitTermination()
    }
}
