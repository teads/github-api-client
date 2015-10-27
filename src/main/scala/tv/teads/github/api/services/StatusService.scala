package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object StatusService extends GithubService with GithubApiCodecs {

  def create(org: String, repository: String, sha: String, status: Status)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[StatusResponse]] = {
    val url = s"${configuration.url}/repos/$org/$repository/statuses/$sha"
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

  def create(repository: String, sha: String, status: Status)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[StatusResponse]] =
    create(configuration.organization, repository, sha, status)

  def fetchStatuses(org: String, repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Status]] =
    fetchMultiple[Status](s"repos/$org/$repository/commits/$ref/statuses", s"Fetching statuses for ref $ref failed")

  def fetchStatuses(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Status]] =
    fetchStatuses(configuration.organization, repository, ref)

  def fetchStatus(org: String, repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[CombinedStatus]] =
    fetchOptional[CombinedStatus](s"repos/$org/$repository/commits/$ref/status", s"Fetching status for ref $ref failed")

  def fetchStatus(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[CombinedStatus]] =
    fetchStatus(configuration.organization, repository, ref)

}
