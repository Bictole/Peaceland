import play.api.libs.json.{Json, OFormat}
import scala.util.Try

final case class SaveableEvent(
                                peacewatcherID : Int,
                                timestamp: String,
                                location: Coords,
                                words: List[String],
                                persons: List[Person]
                              )
object SaveableEvent {
  implicit val SaveableEventFormatter: OFormat[SaveableEvent] = Json.format[SaveableEvent]

  def apply(str: String): Option[SaveableEvent] = {
    Try(Json.parse(str).asOpt[SaveableEvent]).getOrElse(None)
  }
}
