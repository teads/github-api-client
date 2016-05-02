package tv.teads.github.api.model.repositories

import java.time.DayOfWeek
import io.circe._
import tv.teads.github.api.model.DateTimeCodecs.WeekDayDecoder

object HourlyCommitCount {

  implicit final val HourlyCommitCountDecoder = Decoder.instance[HourlyCommitCount] { cursor ⇒
    for {
      dayOfWeek ← cursor.downN(0).as[DayOfWeek](WeekDayDecoder)
      hour ← cursor.downN(1).as[Int]
      commitCount ← cursor.downN(2).as[Int]
    } yield HourlyCommitCount(dayOfWeek, hour, commitCount)
  }
}
case class HourlyCommitCount(
  dayOfWeek:   DayOfWeek,
  hour:        Int,
  commitCount: Int
)
