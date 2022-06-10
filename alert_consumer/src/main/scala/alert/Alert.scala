package alert 

import java.time.LocalDateTime

final case class Alert(
    peacewatcher_id : Int,
    timestamp: LocalDateTime,
    location: Coords,
    words: List[String],
    persons: List[Person]
)
