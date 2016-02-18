package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._

class RateLimitService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  private val DefaultRateLimit = RateLimit(Resources(Core(0, 0, 0), Core(0, 0, 0)))

  def getRateLimit(implicit ec: ExecutionContext): Future[RateLimit] = {
    val requestBuilder = new Request.Builder().url(s"${config.apiUrl}/rate_limit").get()
    baseRequest(requestBuilder).map {
      _.as[RateLimit].fold(
        code ⇒ failedRequest("Fetching rate limit failed", code, DefaultRateLimit),
        _.decoded
      )
    }
  }
}
