package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{HttpHeaders, StatusCodes, HttpRequest}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object TeamService {
  sealed trait MembershipFilter
  object MembershipFilter extends Enumerated[MembershipFilter] {
    val values = List(member, maintainer, all)

    case object member extends MembershipFilter
    case object maintainer extends MembershipFilter
    case object all extends MembershipFilter
  }

  sealed trait Membership
  object Membership extends Enumerated[Membership] {
    val values = List(member, maintainer)

    case object member extends Membership
    case object maintainer extends Membership
  }
}
class TeamService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import TeamService._

  def fetchTeam(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Team]] =
    fetchOptional[Team](s"teams/$id", s"Fetching teams for id $id failed")

  def fetchOrgTeams(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Team]] =
    fetchMultiple[Team](s"orgs/${config.owner}/teams", s"Fetching teams for organization ${config.owner} failed")

  def fetchTeamMembers(id: Long, filter: MembershipFilter = MembershipFilter.all)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Member]] = {
    val req: HttpRequest = Get(s"${config.apiUrl}/teams/$id/members")
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
    val url: String = s"${config.apiUrl}/teams/$id/members/$username"
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
    val url: String = s"${config.apiUrl}/teams/$id/members/$username"
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
    val url: String = s"${config.apiUrl}/teams/$id/members/$username"
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

  def addTeamMembership(id: Long, username: String, membership: Membership)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url: String = s"${config.apiUrl}/teams/$id/memberships/$username"
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
    val url: String = s"${config.apiUrl}/teams/$id/memberships/$username"
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

  def fetchTeamRepos(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] =
    fetchMultiple[Repository](s"teams/$id/repos", s"Fetching repositories for team id $id failed")

  def addTeamRepo(id: Long, repository: String, permission: Permission)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url: String = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
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
    val url: String = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
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
    val url: String = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
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
    val url = s"${config.apiUrl}/orgs/${config.owner}/teams"
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
    val url = s"${config.apiUrl}/teams/$id"
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
    val url = s"${config.apiUrl}/teams/$id"
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
