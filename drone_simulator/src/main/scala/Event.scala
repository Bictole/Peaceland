import java.security.Timestamp
import java.time.LocalDateTime
final case class Event(
    timestamp: LocalDateTime,
    drone_id: Int,
    location: Tuple2[Double, Double],
    persons: List[Person]
)