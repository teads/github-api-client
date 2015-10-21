package tv.teads.github.api.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import play.api.data.mapping._
import play.api.libs.json.{JsString, JsValue}

object CustomWrites {

  implicit val dateTimeWrite = Write[ZonedDateTime, JsValue] { dt ⇒
    JsString(ISO_OFFSET_DATE_TIME.format(dt))
  }

}
