package peacewatcher

import java.time.LocalDateTime

final case class Coords(latitude: Double, longitude: Double)

final case class Event(
    peacewatcherID : Int,
    timestamp: LocalDateTime,
    location: Coords,
    words: List[String],
    persons: List[Person]
)
