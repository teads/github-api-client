package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

class UserService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  def getUserInfo(token: String)(implicit ec: ExecutionContext): Future[Option[User]] =
    fetchOptional[User](
      s"user",
      "Getting user info failed",
      customToken = Some(token)
    )
}
