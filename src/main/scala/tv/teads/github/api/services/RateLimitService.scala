package tv.teads.github.api.services

import tv.teads.github.api.models._
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object RateLimitService extends GithubService {

  def getRateLimit(implicit ec: ExecutionContext): Future[RateLimit] =
    baseRequest(s"${configuration.api.url}/rate_limit", Map.empty)
      .executeRequestInto[RateLimit]().map {
      case SuccessfulRequest(rateLimit, _) ⇒ rateLimit
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not fetch rate limit, failed with status code ${statusCode.intValue}")
        val core = Core(0, 0, 0)
        RateLimit(Resources(core, core), core)
    }
}
