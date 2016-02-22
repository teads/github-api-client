package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

class MemberService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  /**
   * @see https://developer.github.com/v3/orgs/members/#members-list
   * @param ec
   * @return
   */
  def listOrganizationMembers(implicit ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](
      s"orgs/${config.owner}/members",
      s"Fetching organization ${config.owner} members failed"
    )

  /**
   * @see https://developer.github.com/v3/orgs/members/#check-membership
   * @param username
   * @param ec
   * @return
   */
  def checkMembership(username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/members/$username"
    val requestBuilder = new Request.Builder().url(url).get()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response if response.code() == 404 ⇒ false
      case response ⇒
        failedRequest(s"Checking if $username is a member of ${config.owner} failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/orgs/members/#public-members-list
   * @param ec
   * @return
   */
  def listPublicMembers(implicit ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](
      s"orgs/${config.owner}/public_members",
      s"Fetching organization ${config.owner} public members failed"
    )

  /**
   * @see https://developer.github.com/v3/orgs/members/#check-public-membership
   * @param username
   * @param ec
   * @return
   */
  def checkPublicMembership(username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/public_members/$username"
    val requestBuilder = new Request.Builder().url(url).get()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response if response.code() == 404 ⇒ false
      case response ⇒
        failedRequest(s"Checking if $username is a public member of ${config.owner} failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/orgs/members/#get-your-organization-membership
   * @param token
   * @param ec
   * @return
   */
  def getAuthUserMembership(token: String)(implicit ec: ExecutionContext): Future[Option[OrganizationMembership]] =
    fetchOptional[OrganizationMembership](
      s"user/memberships/orgs/${config.owner}",
      s"Getting ${config.owner} organization membership failed",
      customToken = Some(token)
    )
}
