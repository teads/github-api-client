package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{ HttpHeaders, StatusCodes, HttpRequest }
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.models.common.ADTEnum
import tv.teads.github.api.models.Permissions.Permission
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object TeamService extends GithubService with PayloadFormats {

  def fetchOrgTeams(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Team]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"orgs/$org/teams"
    val errorMsg = s"Fetching teams for organization $org failed"
    fetchMultiple[Team](url, errorMsg)
  }

  def fetchDefaultOrgTeams(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Team]] = {
    fetchOrgTeams(configuration.organization)
  }

  sealed trait MembershipFilter

  object MembershipFilter extends ADTEnum[MembershipFilter] {

    case object member extends MembershipFilter
    case object maintainer extends MembershipFilter
    case object all extends MembershipFilter

    val list = Seq(
      member, maintainer, all
    )
  }

  def fetchTeamMembers(id: Long, filter: MembershipFilter = MembershipFilter.all)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/members"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequestInto[List[Member]]().map {
        case SuccessfulRequest(o, _) ⇒ o
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not fetch team $id members, failed with status code ${statusCode.intValue}")
          Nil
      }
  }

  @Deprecated
  def isTeamMember(id: Long, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/members/$username"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not check if user $username is member of team $id , failed with status code ${response.status.intValue}")
          false
      }
  }

  @Deprecated
  def addTeamMember(id: Long, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/members/$username"
    val req: HttpRequest = Put(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.`Content-Length`.name, "0")
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not add user $username as member of team $id, failed with status code ${response.status}")
          false
      }
  }

  @Deprecated
  def deleteTeamMember(id: Long, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/members/$username"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove user $username from team $id, failed with status code ${response.status}")
          false
      }
  }

  sealed trait Membership

  object Membership extends ADTEnum[Membership] {

    case object member extends Membership
    case object maintainer extends Membership

    val list = Seq(
      member, maintainer
    )
  }

  def addTeamMembership(id: Long, username: String, membership: Membership)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/memberships/$username"
    val req: HttpRequest = Put(url, membership)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not add user $username as member of team $id, failed with status code ${response.status}")
          false
      }
  }

  def deleteTeamMembership(id: Long, username: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/memberships/$username"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove user $username from team $id, failed with status code ${response.status}")
          false
      }
  }

  def fetchTeamRepos(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"teams/$id/repos"
    val errorMsg = s"Fetching repositories for team id $id failed"
    fetchMultiple[Repository](url, errorMsg)
  }

  def addTeamRepo(id: Long, repository: String, permission: Permission)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/repos/${configuration.organization}/$repository"
    val req: HttpRequest = Put(url, permission.toString)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not add repository $repository to team $id, failed with status code ${response.status}")
          false
      }
  }

  def deleteTeamRepo(id: Long, repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/repos/${configuration.organization}/$repository"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove repository $repository to team $id, failed with status code ${response.status}")
          false
      }
  }

  def isRepoManagedByTeam(id: Long, repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"${configuration.url}/teams/$id/repos/${configuration.organization}/$repository"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not check if repository $repository is managed by team $id, failed with status code ${response.status}")
          false
      }
  }

  def create(team: Team)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Team]] = {
    val url = s"${configuration.url}/orgs/${configuration.organization}/teams"
    val req: HttpRequest = Post(url, team)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequestInto[Team]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create team, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def edit(id: Long, team: Team)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Team]] = {
    val url = s"${configuration.url}/teams/$id"
    val req: HttpRequest = Patch(url, team)
    baseRequest(req, Map.empty, usePermissionMediaType = true)
      .executeRequestInto[Team]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not edit team $id, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def delete(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/teams/$id"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not delete team $id, failed with status code ${response.status}")
          false
      }
  }
}
