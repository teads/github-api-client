package tv.teads.github.api.services

import java.time.ZonedDateTime

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.{ExecutionContext, Future}

object CommitService {
  case class CommitFilter(
    sha:    Option[String]        = None,
    path:   Option[String]        = None,
    author: Option[String]        = None,
    since:  Option[ZonedDateTime] = None,
    until:  Option[ZonedDateTime] = None
  )
}

class CommitService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import CommitService._

  def list(repository: String, filter: CommitFilter)(implicit ec: ExecutionContext): Future[List[GHCommit]] =
    fetchMultiple[GHCommit](
      s"repos/${config.owner}/$repository/commits",
      s"Fetching $repository commits failed",
      filter.toMapStringified
    )

  def fetch(repository: String, sha: String)(implicit ec: ExecutionContext): Future[Option[GHCommit]] =
    fetchOptional[GHCommit](
      s"repos/${config.owner}/$repository/commits/$sha",
      s"Fetching $repository commit $sha failed"
    )

  def compare(repository: String, base: String, head: String)(implicit ec: ExecutionContext): Future[Option[Comparison]] =
    fetchOptional[Comparison](
      s"repos/${config.owner}/$repository/compare/$base...$head",
      s"Fetching $repository compare between $base and $head failed"
    )

}
