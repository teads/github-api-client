package tv.teads.github.api.model.repositories

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.DateTimeCodecs.TimestampSecondsDecoder

object WeeklyAdditionsDeletions {
  implicit final val WeeklyAdditionsDeletionsDecoder = Decoder.instance[WeeklyAdditionsDeletions] { cursor ⇒
    for {
      weekStart ← cursor.downN(0).as(TimestampSecondsDecoder)
      additions ← cursor.downN(1).as[Int]
      deletions ← cursor.downN(2).as[Int]
    } yield WeeklyAdditionsDeletions(weekStart, additions, deletions)
  }
}

case class WeeklyAdditionsDeletions(
  weekStart: ZonedDateTime,
  additions: Int,
  deletions: Int
)
