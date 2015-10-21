package tv.teads.github.api.util

import java.time._
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.time.temporal._

import scala.language.implicitConversions
import scala.util.Try

import play.api.data.mapping._
import play.api.libs.json.{JsNumber, JsString, JsValue}

object CustomRules {

  private implicit def toTemporalQuery[R](f: TemporalAccessor ⇒ R): TemporalQuery[R] = new TemporalQuery[R] {
    override def queryFrom(temporal: TemporalAccessor): R = f(temporal)
  }

  private val InvalidZonedDateTime = Failure(Seq(ValidationError("error.ZonedDateTime")))

  val zonedDateTime = Rule.fromMapping[JsValue, ZonedDateTime] {
    case JsNumber(v) ⇒
      Success(ZonedDateTime.ofInstant(Instant.ofEpochMilli(v.toLong), ZoneId.systemDefault()))
    case JsString(v) ⇒ Try(ISO_OFFSET_DATE_TIME.parse(v, ZonedDateTime.from _)).toOption match {
      case Some(dt) ⇒ Success(dt)
      case None     ⇒ InvalidZonedDateTime
    }
    case _ ⇒ InvalidZonedDateTime
  }

}
