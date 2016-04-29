package tv.teads.github.api.model

import java.time._
import java.time.format.DateTimeFormatter

import io.circe.Decoder
import io.circe.java8.time._

object DateTimeCodecs {
  private val UtcZoneId = ZoneOffset.UTC.normalized()

  implicit final val WeekDayDecoder = Decoder.instance[DayOfWeek](
    _.as[Int].map(dayNumber ⇒ DayOfWeek.of(dayNumber + 1).minus(1))
  )

  implicit final val TimestampSecondsDecoder = Decoder.instance[ZonedDateTime](
    _.as[Long].map(Instant.ofEpochSecond).map(instant ⇒ ZonedDateTime.ofInstant(instant, UtcZoneId))
  )

}
