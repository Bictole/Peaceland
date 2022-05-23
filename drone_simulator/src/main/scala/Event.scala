import java.security.Timestamp
final case class Event(
    timestamp: Timestamp,
    drone_id: Int,
    location: Tuple2[Double, Double],
    persons: List[Person]
)