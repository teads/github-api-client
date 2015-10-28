package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

class StatusService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  def create(repository: String, sha: String, status: Status)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[StatusResponse]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/statuses/$sha"
    val req: HttpRequest = Post(url, status)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[StatusResponse]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create status with url $url, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def fetchStatuses(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Status]] =
    fetchMultiple[Status](s"repos/${config.owner}/$repository/commits/$ref/statuses", s"Fetching statuses for ref $ref failed")

  def fetchStatus(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[CombinedStatus]] =
    fetchOptional[CombinedStatus](s"repos/${config.owner}/$repository/commits/$ref/status", s"Fetching status for ref $ref failed")
}
