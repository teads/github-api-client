package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object MemberService extends GithubService {

  def fetchOrgMembers(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"orgs/$org/members"
    val errorMsg = s"Fetching organization $org members failed"
    fetchMultiple[Member](url, errorMsg)
  }

  def fetchDefaultOrgMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    fetchOrgMembers(configuration.organization)
  }

  def isMemberOfOrg(org: String, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/orgs/$org/members/$username"
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

  def isMemberOfDefaultOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    isMemberOfOrg(configuration.organization, username)
  }

  def fetchOrgPublicMembers(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"orgs/$org/public_members"
    val errorMsg = s"Fetching organization $org public members failed"
    fetchMultiple[Member](url, errorMsg)
  }

  def fetchDefaultOrgPublicMembers(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    fetchOrgPublicMembers(configuration.organization)
  }

  def isPublicMemberOfOrg(org: String, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/orgs/$org/public_members/$username"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not check if $username is a public member of $org, failed with status code ${response.status}")
          false
      }
  }

  def isPublicMemberOfDefaultOrg(username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    isPublicMemberOfOrg(configuration.organization, username)
  }

}
