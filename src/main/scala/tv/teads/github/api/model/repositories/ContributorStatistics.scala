package tv.teads.github.api.model.repositories

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.common.{GithubAccount, GithubAccountCodec}
import tv.teads.github.api.model.DateTimeCodecs.timestampSecondsDecoder

trait ContributorWeeklyStatsCodec {
  implicit val contributorWeeklyStatsDecoder =
    Decoder.forProduct4("w", "a", "d", "c")(ContributorWeeklyStats.apply)
}
case class ContributorWeeklyStats(
  weekStart: ZonedDateTime,
  additions: Int,
  deletions: Int,
  commits:   Int
)

trait ContributorStatisticsCodec
    extends ContributorWeeklyStatsCodec with GithubAccountCodec {

  implicit val contributorStatisticsDecoder =
    Decoder.forProduct3("author", "total", "weeks")(ContributorStatistics.apply)

}
case class ContributorStatistics(
  author:      GithubAccount,
  total:       Int,
  weeklyStats: List[ContributorWeeklyStats]
)
