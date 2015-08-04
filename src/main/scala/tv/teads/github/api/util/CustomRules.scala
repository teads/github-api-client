package tv.teads.github.api.util

import org.joda.time.DateTime
import play.api.data.mapping.json.Rules._
import play.api.data.mapping._
import play.api.libs.json.{ JsNumber, JsString, JsValue }

object CustomRules {

  implicit val jodaDate = jodaDateRule("yyyy-MM-dd'T'HH:mm:ssZ")

  def jodaLongOrISO: Rule[JsValue, DateTime] = {
    val invalid = Failure(Seq(ValidationError("error.DateTime")))

    Rule.fromMapping[JsValue, DateTime] {
      case JsNumber(v) ⇒ jodaTime.validate(v.toLong).asOpt match {
        case Some(dt) ⇒ Success(dt)
        case None     ⇒ invalid
      }
      case JsString(v) ⇒ jodaDate.validate(v).asOpt match {
        case Some(dt) ⇒ Success(dt)
        case None     ⇒ invalid
      }
      case _ ⇒ invalid
    }
  }

}
