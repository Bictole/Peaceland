package peacewatcher

import play.api.libs.json._
import java.time.LocalDateTime
import scala.util.Random
import scala.io.Source

object Main {
    
    def generateData(names: JsArray, words: List[String]): Event = {
        val r = scala.util.Random
        val size = names.value.size
        Event(
            r.nextInt,
            LocalDateTime.now(),
            Coords(r.nextDouble, r.nextDouble),
            List(words(r.nextInt(words.size))),
            List(
                Person(
                    (names.value(r.nextInt(size)) \ "name").as[String],
                    r.nextDouble,
                )
            )
        )
    }
    
    def main(args: Array[String]): Unit = {
        
        // Declare classes format to serialize
        implicit val personWrites = Json.format[Person]
        implicit val coordsWrites = Json.format[Coords]
        implicit val eventWrites = Json.format[Event]

        // Import citizens'names
        val namesFileName = "src/main/resources/name.json"
        val fSource = Source.fromFile(namesFileName)
        val namesRaw = fSource.getLines.mkString
        fSource.close()
        val namesJson = Json.parse(namesRaw)
        
        // List of words that peacewatchers can detect
        val phrases = List("Hello", "Goodbye", "Help me!", "Mayday", "Scala is a plusgood language", "I come in peace! Don't shoot!")
        
        // Generate events
        val event = generateData(namesJson.as[JsArray], phrases)
        val eventJsonString = Json.stringify(Json.toJson(event))
        println(eventJsonString)
    }
}
