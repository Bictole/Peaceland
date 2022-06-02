package peacewatcher

import java.util.Properties
import play.api.libs.json._
import java.time.LocalDateTime
import scala.util.Random
import scala.io.Source
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
        
object Main {
    
    def generateData(names: JsArray, words: List[String], producer: KafkaProducer[String, String]) = {
        
        // Declare classes format to serialize
        implicit val personWrites = Json.format[Person]
        implicit val coordsWrites = Json.format[Coords]
        implicit val eventWrites = Json.format[Event]

        val size = names.value.size
        (0 to 10).foreach((i) => {
            val event = Event(
                Random.nextInt,
                LocalDateTime.now(),
                Coords(Random.nextDouble, Random.nextDouble),
                List(words(Random.nextInt(words.size))),
                (0 to Random.nextInt(5)).map(p => 
                        Person(
                            (names.value(Random.nextInt(size)) \ "name").as[String],
                            Random.nextDouble
                        )).toList
            )
            val eventJsonString = Json.stringify(Json.toJson(event))
            println(eventJsonString)
            val record = new ProducerRecord[String, String](Array("peaceland"), "event", eventJsonString)
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
        val namesFileName = "src/main/resources/name.json"
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
