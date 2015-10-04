package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.HttpRequest
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object UserService extends GithubService with PayloadFormats {

  def getUserInfo(token: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[User]] = {
    val url = s"${configuration.api.url}/user"
    baseRequest(Get(url), Map.empty, Some(token)).executeRequestInto[User]().map {
      case SuccessfulRequest(userInfo, _) ⇒ Some(userInfo)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Getting user info failed, status code: ${statusCode.intValue}")
        None
    }
  }
}
