package data

import play.api.libs.json.{Json, OFormat}

final case class Person(
                        name: String,
                        peacescore: Double,
                       )
object Person {
    implicit val PersonFormatter: OFormat[Person] = Json.format[Person]
}