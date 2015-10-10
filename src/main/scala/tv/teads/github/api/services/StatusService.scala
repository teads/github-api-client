package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import play.api.data.mapping.{To, Write}
import play.api.libs.json.{JsObject, JsValue}
import shapeless._
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration
import tv.teads.github.api.filters.common.Directions.Direction
import tv.teads.github.api.filters.common.States.State
import tv.teads.github.api.models.StatusStates.StatusState
import tv.teads.github.api.models._
import tv.teads.github.api.models.common.ADTEnum
import tv.teads.github.api.models.payloads.PayloadFormats
import Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object StatusService extends GithubService with PayloadFormats {

  def create(repository: String, sha: String, status: Status)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[StatusResponse]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/statuses/$sha"
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

}
