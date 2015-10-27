package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object MemberService extends GithubService with GithubApiCodecs {

  def fetchOrgMembers(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](s"orgs/$org/members", s"Fetching organization $org members failed")

  def fetchDefaultOrgMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchOrgMembers(configuration.organization)

  def isMemberOfOrg(org: String, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/orgs/$org/members/$username"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not check if $username is a member of $org, failed with status code ${response.status}")
          false
      }
  }

  def isMemberOfDefaultOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] =
    isMemberOfOrg(configuration.organization, username)

  def fetchOrgPublicMembers(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchMultiple[Member](s"orgs/$org/public_members", s"Fetching organization $org public members failed")

  def fetchDefaultOrgPublicMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] =
    fetchOrgPublicMembers(configuration.organization)

  def isPublicMemberOfOrg(org: String, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    baseRequest(Get(s"${configuration.url}/orgs/$org/public_members/$username"), Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not check if $username is a public member of $org, failed with status code ${response.status}")
          false
      }
  }

  def isPublicMemberOfDefaultOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] =
    isPublicMemberOfOrg(configuration.organization, username)

  def getAuthUserMembership(org: String, token: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[OrganizationMembership]] = {
    baseRequest(Get(s"${configuration.url}/user/memberships/orgs/$org"), Map.empty, Some(token)).executeRequestInto[OrganizationMembership]().map {
      case SuccessfulRequest(o, _) ⇒ Some(o)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Getting $org organization membership failed, status code: ${statusCode.intValue}")
        None
    }
  }

  def getAuthUserMembershipOfDefaultOrg(token: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[OrganizationMembership]] =
    getAuthUserMembership(configuration.organization, token)
}
