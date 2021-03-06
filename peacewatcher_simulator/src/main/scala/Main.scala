package peacewatcher_simulator

import java.util.Properties
import play.api.libs.json._
import java.time.LocalDateTime
import scala.util.Random
import scala.io.Source
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer

import org.apache.log4j.{Level, Logger}

import data._
        
object Main {
    
    def generateData(names: JsArray, words: List[String], producer: KafkaProducer[String, String]) = {
        // keep only the errors
        Logger.getLogger("org").setLevel(Level.ERROR)
        
        val size = names.value.size
        (0 to 1000).foreach((i) => {
            val event = Event(
                Random.between(10000, 100000),
                LocalDateTime.now(),
                Coords(Random.between(-90.00, 90.00), Random.between(-90.00, 90.00)),
                (1 to Random.between(1, 5)).map(p => words(Random.nextInt(words.size))).toList,
                (1 to Random.between(1, 10)).map(p => 
                        Person(
                            (names.value(Random.nextInt(size)) \ "name").as[String],
                            Random.nextDouble
                        )).toList,
                Random.between(0, 100),
                Random.between(-30, 50)
            )
            val eventJsonString = Json.stringify(Json.toJson(event))
            println(eventJsonString)
            val record = new ProducerRecord[String, String]("peaceland", "event", eventJsonString)
            producer.send(record)
            Thread.sleep(1000)
        })
    }
    
    def main(args: Array[String]): Unit = {
        
        val props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])


        val producer : KafkaProducer[String, String] = new KafkaProducer[String, String](props)

        // Import citizens'names
        val namesFileName = "peacewatcher_simulator/src/main/resources/name.json"
        val fSource = Source.fromFile(namesFileName)
        val namesRaw = fSource.getLines.mkString
        fSource.close()
        val namesJson = Json.parse(namesRaw)
        
        // List of words that peacewatchers can detect
        val phrases = List("Hello", "Goodbye", "Help me!", "Mayday", "Scala is a plusgood language", "I come in peace! Don't shoot!")
        
        // Generate events
        generateData(namesJson.as[JsArray], phrases, producer)

        producer.close()
        
    }
}
