package tv.teads.github.api.services.repositories

import io.circe.generic.auto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.repositories._
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class RepositoriesStatisticsService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def contributorsStatistics(repository: String)(implicit ec: EC): Future[List[ContributorStatistics]] =
    jsonNilIfFailed[ContributorStatistics](
      getCall(s"repos/${config.owner}/$repository/stats/contributors"),
      s"Could not fetch contributors statistics for repository $repository"
    )

  def lastYearCommitActivity(repository: String)(implicit ec: EC): Future[List[WeeklyCommitCount]] =
    jsonNilIfFailed[WeeklyCommitCount](
      getCall(s"repos/${config.owner}/$repository/stats/commit_activity"),
      s"Could not fetch last year's commit activity for repository $repository"
    )

  def weeklyAdditionsDeletions(repository: String)(implicit ec: EC): Future[List[WeeklyAdditionsDeletions]] =
    jsonNilIfFailed[WeeklyAdditionsDeletions](
      getCall(s"repos/${config.owner}/$repository/stats/code_frequency"),
      s"Could not fetch weekly additions/deletions for repository $repository"
    )

  def lastYearWeeklyCommitCount(repository: String)(implicit ec: EC): Future[Option[YearlyWeeklyCommitCount]] =
    jsonOptionalIfFailed[YearlyWeeklyCommitCount](
      getCall(s"repos/${config.owner}/$repository/stats/participation"),
      s"Could not fetch last year's weekly commit count for repository $repository"
    )

  def hourlyCommitCount(repository: String)(implicit ec: EC): Future[List[HourlyCommitCount]] =
    jsonNilIfFailed[HourlyCommitCount](
      getCall(s"repos/${config.owner}/$repository/stats/punch_card"),
      s"Could not fetch hourly commit count for repository $repository"
    )
}
