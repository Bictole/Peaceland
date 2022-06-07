package saveaggregate

import java.sql.Date

final case class Coords(latitude: Double, longitude: Double)

final case class Event(
    peacewatcher_id : Int,
    timestamp: Date,
    location: Coords,
    words: List[String],
    persons: List[Person]
)
