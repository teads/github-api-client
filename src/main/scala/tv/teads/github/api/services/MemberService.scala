package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

class MemberService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  def fetchOrgMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](
      s"orgs/${config.owner}/members",
      s"Fetching organization ${config.owner} members failed"
    )

  def isMemberOfOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/members/$username"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not check if $username is a member of ${config.owner}, failed with status code ${response.status}")
          false
      }
  }

  def fetchOrgPublicMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](s"orgs/${config.owner}/public_members", s"Fetching organization ${config.owner} public members failed")

  def isPublicMemberOfOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    baseRequest(Get(s"${config.apiUrl}/orgs/${config.owner}/public_members/$username"), Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not check if $username is a public member of ${config.owner}, failed with status code ${response.status}")
          false
      }
  }

  def getAuthUserMembership(token: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[OrganizationMembership]] = {
    baseRequest(Get(s"${config.apiUrl}/user/memberships/orgs/${config.owner}"), Map.empty, Some(token)).executeRequestInto[OrganizationMembership]().map {
      case SuccessfulRequest(o, _) ⇒ Some(o)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Getting ${config.owner} organization membership failed, status code: ${statusCode.intValue}")
        None
    }
  }
}
