package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.HttpRequest
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object RateLimitService extends GithubService with PayloadFormats {

  def getRateLimit(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[RateLimit] = {
    val url: String = s"${configuration.url}/rate_limit"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .executeRequestInto[RateLimit]().map {
        case SuccessfulRequest(rateLimit, _) ⇒ rateLimit
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not fetch rate limit, failed with status code ${statusCode.intValue}")
          val core = Core(0, 0, 0)
          RateLimit(Resources(core, core))
      }
  }
}
