import play.api.libs.json._
import java.time.LocalDateTime
import scala.io.Source
import scala.util.Random

object Main extends App {
    val p = Person("Alexandre L", 20.3211, "Blablabla")
    val event = Event(LocalDateTime.now(), 4242, (23.04523, -11.03435), List(p))
    implicit val personWrites = Json.format[Person]
    implicit val eventWrites = Json.format[Event]
    val eventJsonString = Json.stringify(Json.toJson(event))
    println(eventJsonString)

    println(generateData)


    def generateData(): Event = {
        val r = scala.util.Random
        Event(
            LocalDateTime.now(),
            r.nextInt,
            (r.nextDouble, r.nextDouble),
            List(Person(r.nextString(20), r.nextDouble, r.nextString(20)))
        )
    }
  
    val namesFileName = "src/main/resource/name.json"
    //val phrases = List("Hello", "Goodbye", "Help me!", "Mayday", "Scala is a plusgood language", "I come in peace! Don't shoot!")

    val fSource = Source.fromFile(namesFileName)
    val namesRaw = fSource.getLines.mkString
    fSource.close()
    val namesJson = Json.parse(namesRaw)
    //println(namesJson)
}
