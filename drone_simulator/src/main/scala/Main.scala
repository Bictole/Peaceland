import play.api.libs.json._

object Main extends App {
  val p = Person("Alexandre L", 20.3211, "Blablabla")
  println(Json.stringify(Json.toJson(p)))
}
