package peacewatcher

import play.api.libs.json._
import java.time.LocalDateTime
import scala.util.Random
import scala.io.Source

object Main {
    
    def generateData(names: JsArray, words: List[String]) = {
        
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
            Thread.sleep(1000)
        })

        
        
    }
    
    def main(args: Array[String]): Unit = {
        
                // Import citizens'names
        val namesFileName = "src/main/resources/name.json"
        val fSource = Source.fromFile(namesFileName)
        val namesRaw = fSource.getLines.mkString
        fSource.close()
        val namesJson = Json.parse(namesRaw)
        
        // List of words that peacewatchers can detect
        val phrases = List("Hello", "Goodbye", "Help me!", "Mayday", "Scala is a plusgood language", "I come in peace! Don't shoot!")
        
        // Generate events
        generateData(namesJson.as[JsArray], phrases)
        
    }
}
