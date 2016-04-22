package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{RateLimit, RateLimitCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

class RateLimitService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with RateLimitCodec {

  def get(implicit ec: ExecutionContext): Future[RateLimit] =
    get[RateLimit](
      "rate_limit",
      "Could not fetch rate limit"
    )
}
