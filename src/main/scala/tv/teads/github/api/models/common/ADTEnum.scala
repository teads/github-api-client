package tv.teads.github.api.models.common

import play.api.libs.json._
import play.api.data.mapping._

// Enum type based on Sum types (OR)
trait ADTEnum[A] {

  val list: Seq[A]

  def withName(name: String): Option[A] = {
    list.find(_.toString.toLowerCase == name.toLowerCase)
  }

  implicit lazy val jsonVReads: Rule[JsValue, A] = Rule.fromMapping {
    case JsString(str) ⇒
      withName(str) match {
        case Some(name) ⇒ Success(name)
        case None       ⇒ Failure(Seq(ValidationError(s"error.unknownADTEnum")))
      }
    case _ ⇒ Failure(Seq(ValidationError(s"error.ADTEnumMustBeAString")))
  }

  implicit lazy val jsonVWrites: Write[A, JsValue] = Write(s ⇒ JsString(s.toString))

  import play.api.libs.json._

  implicit lazy val jsonWrites: play.api.libs.json.Writes[A] = play.api.libs.json.Writes(s ⇒ JsString(s.toString))

}

object enums {

  trait EnumAdt[A] {
    def values: List[A]

    def valueAsString(x: A): String

    def parseValue(x: String): Option[A] = values.find(valueAsString(_) == x)
  }

  def values[A](implicit ev: EnumAdt[A]): List[A] = ev.values
}