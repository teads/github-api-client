package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

class UserService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  /**
   * @see https://developer.github.com/v3/users/#get-the-authenticated-user
   * @param token
   * @param ec
   * @return
   */
  def getAuthenticatedUser(token: String)(implicit ec: ExecutionContext): Future[Option[User]] =
    fetchOptional[User](
      s"user",
      "Getting user info failed",
      customToken = Some(token)
    )
}
