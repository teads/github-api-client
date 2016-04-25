package tv.teads.github.api.model.repositories

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.DateTimeCodecs.timestampSecondsDecoder

trait WeeklyAdditionsDeletionsCodec {
  implicit val weeklyAdditionsDeletionsDecoder = Decoder.instance[WeeklyAdditionsDeletions] { cursor ⇒
    for {
      weekStart ← cursor.downN(0).as(timestampSecondsDecoder)
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