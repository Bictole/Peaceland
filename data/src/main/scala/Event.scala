package data

import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}

final case class Coords(
                        latitude: Double,
                        longitude: Double
                        )
object Coords {
    implicit val CoordsFormatter: OFormat[Coords] = Json.format[Coords]
}

final case class Event(
                        peacewatcher_id : Int, 
                        timestamp: LocalDateTime,
                        location: Coords,
                        words: List[String],
                        persons: List[Person],
                        battery: Int,
                        temperature: Int
                    )
object Event {
    implicit val EventFormatter: OFormat[Event] = Json.format[Event]
}
