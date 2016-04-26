package tv.teads.github.api.model

import java.time._

import io.circe.Decoder

object DateTimeCodecs {
  private val UtcZoneId = ZoneOffset.UTC.normalized()

  implicit final val weekDayDecoder = Decoder.instance[DayOfWeek](
    _.as[Int].map(dayNumber ⇒ DayOfWeek.of(dayNumber + 1).minus(1))
  )

  implicit final val timestampSecondsDecoder = Decoder.instance[ZonedDateTime](
    _.as[Long]
      .map(Instant.ofEpochSecond)
      .map(instant ⇒ ZonedDateTime.ofInstant(instant, UtcZoneId))
  )

}
