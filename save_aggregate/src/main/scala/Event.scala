import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}
import scala.util.Try

final case class Coords(latitude: Double, longitude: Double)
object Coords {
  implicit val CoordsFormatter: OFormat[Coords] = Json.format[Coords]
  def apply(str: String): Option[Coords] = {
    Try(Json.parse(str).asOpt[Coords]).getOrElse(None)
  }
}

final case class Event( peacewatcherID : Int, timestamp: LocalDateTime, location: Coords, words: List[String], persons: List[Person])
object Event {
  implicit val EventFormatter: OFormat[Event] = Json.format[Event]

  def apply(str: String): Option[Event] = {
    Try(Json.parse(str).asOpt[Event]).getOrElse(None)
  }
}
