package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorRefFactory
import spray.http.HttpRequest
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._
import tv.teads.github.api.util._

object RateLimitService extends GithubService with GithubApiCodecs {

  def getRateLimit(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[RateLimit] = {
    val req: HttpRequest = Get(s"${configuration.url}/rate_limit")
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
