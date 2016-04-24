package tv.teads.github.api.model

import java.time._
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.time.temporal.{TemporalAccessor, TemporalQuery}

import cats.data.Xor
import io.circe._
import io.circe.syntax._

import scala.language.implicitConversions
import scala.util.Try

private[api] trait ZonedDateTimeCodec {
  private implicit def toTemporalQuery[R](f: TemporalAccessor ⇒ R): TemporalQuery[R] = new TemporalQuery[R] {
    override def queryFrom(temporal: TemporalAccessor): R = f(temporal)
  }

  implicit lazy val ZonedDateTimeDecoder = Decoder.instance[ZonedDateTime] { cursor ⇒
    zonedDateTimeFromString(cursor) orElse zonedDateTimeFromLong(cursor)
  }

  implicit lazy val ZonedDateTimeEncoder = Encoder.instance[ZonedDateTime] { dt ⇒
    ISO_OFFSET_DATE_TIME.format(dt).asJson
  }

  private def zonedDateTimeFromString(cursor: HCursor): Xor[DecodingFailure, ZonedDateTime] =
    Xor.fromOption(cursor.focus.asString, DecodingFailure("notAString", cursor.history)).flatMap { s ⇒
      Xor.fromTry(Try(ISO_OFFSET_DATE_TIME.parse(s, ZonedDateTime.from _)))
        .leftMap(_ ⇒ DecodingFailure("java.time.ZonedDateTime", cursor.history))
    }

  private def zonedDateTimeFromLong(cursor: HCursor) =
    Xor.fromOption(cursor.focus.asNumber, DecodingFailure("NaN", cursor.history)).map { number ⇒
      val instant = Instant.ofEpochMilli(number.truncateToLong)
      ZonedDateTime.ofInstant(instant, ZoneOffset.UTC.normalized())
    }
}
