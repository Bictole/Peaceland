package alert_consumer

import java.util.Properties
import play.api.libs.json._
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

import org.apache.log4j.{Level, Logger}

import data._

object Main{

    def main(args: Array[String]): Unit = {
        // keep only the errors
        Logger.getLogger("org").setLevel(Level.ERROR)

        val sparkConf = new SparkConf()
            .setAppName("peaceland_alert")
            .setMaster("local[*]")
            .set("spark.driver.host", "127.0.0.1")

        val ssc = new StreamingContext(sparkConf, Seconds(5))

        val kafkaParams = Map(
            "bootstrap.servers" -> "localhost:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "alert_consumer"
        )
        
        val topics = Array("peaceland")

        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String,String](topics, kafkaParams)
        )

        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
        
        val sparkContext = ssc.sparkContext
        val kafkaSink = sparkContext.broadcast(KafkaSink(props))

        stream.flatMap(record => {
            val json = Json.parse(record.value())
            Event.EventFormatter.reads(json).asOpt
        }).filter(event => {
            val dangerous_persons = event.persons.filter(person => person.peacescore < 0.1)
            dangerous_persons.foreach(person => println(s"[ALERT] ${person.name} is dangerous with ${person.peacescore} as peacescore."))
            dangerous_persons.length != 0
        }).map({event =>
            val new_alert = Alert(event.peacewatcher_id, event.timestamp, event.location, event.words, event.persons.filter(person => person.peacescore < 0.1))
            val alertJsonString = Json.stringify(Json.toJson(new_alert))
            alertJsonString
        }).foreachRDD({ rdd =>
            rdd.foreach({alertJsonString =>
                kafkaSink.value.send("alert", "event_alert", alertJsonString)
          })
        })
        
        ssc.start()
        ssc.awaitTermination()
    }
}