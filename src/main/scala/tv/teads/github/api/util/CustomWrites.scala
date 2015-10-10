package tv.teads.github.api.util

import org.joda.time.DateTime
import play.api.data.mapping._
import play.api.data.mapping.json.Rules._
import play.api.libs.json.{JsNumber, JsString, JsValue}

object CustomWrites {

  implicit val dateTimeToStringJsValue: Write[DateTime, JsValue] = Write[DateTime, JsValue] { dt ⇒ JsString(dt.toString) }

}
