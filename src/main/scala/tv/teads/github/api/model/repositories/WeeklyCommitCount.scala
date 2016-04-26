package tv.teads.github.api.model.repositories

import io.circe._
import java.time.ZonedDateTime
import tv.teads.github.api.model.DateTimeCodecs.timestampSecondsDecoder

object WeeklyCommitCount {
  implicit final val WeeklyCommitCountDecoder =
    Decoder.forProduct3("days", "total", "week")(WeeklyCommitCount.apply)
}
case class WeeklyCommitCount(
  days:      List[Int],
  total:     Int,
  weekStart: ZonedDateTime
)
