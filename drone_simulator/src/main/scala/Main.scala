import play.api.libs.json._
import java.time.LocalDateTime
import scala.io.Source
import scala.util.Random

object Main {
    val p = Person("Alexandre L", 20.3211, "Blablabla")
    val event = Event(LocalDateTime.now(), 4242, (23.04523, -11.03435), List(p))
    implicit val personWrites = Json.format[Person]
    implicit val eventWrites = Json.format[Event]
    val eventJsonString = Json.stringify(Json.toJson(event))
    println(eventJsonString)


    def generateData(names: JsArray): Event = {
        val r = scala.util.Random
        val size = names.value.size
        Event(
            LocalDateTime.now(),
            r.nextInt,
            (r.nextDouble, r.nextDouble),
            List(Person(
              (names.value(r.nextInt(size)) \ "name").as[String],
              r.nextDouble,
              r.nextString(20)
            ))
        )
    }

    def main(args: Array[String]): Unit = {
        val namesFileName = "src/main/resource/name.json"
        //val phrases = List("Hello", "Goodbye", "Help me!", "Mayday", "Scala is a plusgood language", "I come in peace! Don't shoot!")

        val fSource = Source.fromFile(namesFileName)
        val namesRaw = fSource.getLines.mkString
        fSource.close()
        val namesJson = Json.parse(namesRaw)
        //println(namesJson)

        println(generateData(namesJson.as[JsArray]))
    }
}
