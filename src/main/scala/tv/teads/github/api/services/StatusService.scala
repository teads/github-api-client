package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object StatusService extends GithubService with PayloadFormats {

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

  def create(repository: String, sha: String, status: Status)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[StatusResponse]] = {
    create(configuration.organization, repository, sha, status)
  }

  def fetchStatuses(org: String, repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Status]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"repos/$org/$repository/commits/$ref/statuses"
    val errorMsg = s"Fetching statuses for ref $ref failed"
    fetchMultiple[Status](url, errorMsg)
  }

  def fetchStatuses(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Status]] = {
    fetchStatuses(configuration.organization, repository, ref)
  }

  def fetchStatus(org: String, repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[CombinedStatus]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"repos/$org/$repository/commits/$ref/status"
    val errorMsg = s"Fetching status for ref $ref failed"
    fetchOptional[CombinedStatus](url, errorMsg)
  }

  def fetchStatus(repository: String, ref: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[CombinedStatus]] = {
    fetchStatus(configuration.organization, repository, ref)
  }

}
