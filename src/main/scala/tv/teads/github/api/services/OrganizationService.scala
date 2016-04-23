package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import tv.teads.github.api.model._
import tv.teads.github.api.GithubApiClientConfig

class OrganizationService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  /**
   * @see https://developer.github.com/v3/orgs/#get-an-organization
   * @param ec
   * @return
   */
  def get(implicit ec: ExecutionContext): Future[Option[Organization]] =
    fetchOptional[Organization](
      s"orgs/${config.owner}",
      s"Fetching organization ${config.owner} failed"
    )

  /**
   * @see https://developer.github.com/v3/orgs/#list-user-organizations
   * @param user
   * @param ec
   * @return
   */
  def listForUser(user: String)(implicit ec: ExecutionContext): Future[List[Org]] =
    fetchMultiple[Org](
      s"users/$user/orgs",
      s"Fetching organizations for user $user failed"
    )

}
