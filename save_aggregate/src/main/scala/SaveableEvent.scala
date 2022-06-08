package saveaggregate

final case class SaveableEvent(
    peacewatcherID : Int,
    timestamp: String,
    location: Coords,
    words: List[String],
    persons: List[Person]
)
