package saveaggregate
import play.api.libs.json.{Json, OFormat}
import scala.util.Try

final case class Person(
                         name: String,
                         peacescore: Double,
                       )
object Person {
  implicit val PersonFormatter: OFormat[Person] = Json.format[Person]
  def apply(str: String): Option[Person] = {
    Try(Json.parse(str).asOpt[Person]).getOrElse(None)
  }
}