package tv.teads.github.api.model.repositories

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.common.GithubAccount
import tv.teads.github.api.model.DateTimeCodecs.TimestampSecondsDecoder

object ContributorWeeklyStats {
  implicit final val ContributorWeeklyStatsDecoder =
    Decoder.forProduct4("w", "a", "d", "c")(ContributorWeeklyStats.apply)
}
case class ContributorWeeklyStats(
  weekStart: ZonedDateTime,
  additions: Int,
  deletions: Int,
  commits:   Int
)

object ContributorStatistics {
  implicit final val ContributorStatisticsDecoder =
    Decoder.forProduct3("author", "total", "weeks")(ContributorStatistics.apply)

}
case class ContributorStatistics(
  author:      GithubAccount,
  total:       Int,
  weeklyStats: List[ContributorWeeklyStats]
)
