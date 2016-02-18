package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._
import tv.teads.github.api.util._

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

  def fetchTeam(id: Long)(implicit ec: ExecutionContext): Future[Option[Team]] =
    fetchOptional[Team](
      s"teams/$id",
      s"Fetching teams for id $id failed"
    )

  def fetchOrgTeams(implicit ec: ExecutionContext): Future[List[Team]] =
    fetchMultiple[Team](
      s"orgs/${config.owner}/teams",
      s"Fetching teams for organization ${config.owner} failed"
    )

  def fetchTeamMembers(id: Long)(implicit ec: ExecutionContext): Future[List[Member]] = {
    fetchMultiple[Member](
      s"teams/$id/members",
      s"Fetching team $id members failed"
    )
  }

  @Deprecated
  def isTeamMember(id: Long, username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/members/$username"
    val requestBuilder = new Request.Builder().url(url).get()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Checking if user $username is member of team $id failed", response.code(), false)
    }
  }

  @Deprecated
  def addTeamMember(id: Long, username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/members/$username"
    val requestBuilder = new Request.Builder().url(url).put(emptyRequestBody)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Adding user $username as member of team $id failed", response.code(), false)
    }
  }

  @Deprecated
  def deleteTeamMember(id: Long, username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/members/$username"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing user $username from team $id failed", response.code(), false)
    }
  }

  def addTeamMembership(id: Long, username: String, membership: Membership)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/memberships/$username"
    val requestBuilder = new Request.Builder().url(url).put(Map("body" → membership).toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 200 ⇒ true
      case response ⇒
        failedRequest(s"Adding user $username as member of team $id failed", response.code(), false)
    }
  }

  def deleteTeamMembership(id: Long, username: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/memberships/$username"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing user $username from team $id failed", response.code(), false)
    }
  }

  def fetchTeamRepos(id: Long)(implicit ec: ExecutionContext): Future[List[Repository]] =
    fetchMultiple[Repository](
      s"teams/$id/repos",
      s"Fetching repositories for team id $id failed"
    )

  def addTeamRepo(id: Long, repository: String, permission: Permission)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
    val requestBuilder = new Request.Builder().url(url).put(Map("body" → permission).toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Adding repository $repository to team $id failed", response.code(), false)
    }
  }

  def deleteTeamRepo(id: Long, repository: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing repository $repository to team $id failed", response.code(), false)
    }
  }

  def isRepoManagedByTeam(id: Long, repository: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id/repos/${config.owner}/$repository"
    val requestBuilder = new Request.Builder().url(url).get()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Checking if repository $repository is managed by team $id failed", response.code(), false)
    }
  }

  def create(team: Team)(implicit ec: ExecutionContext): Future[Option[Team]] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/teams"
    val requestBuilder = new Request.Builder().url(url).post(team.toJson)
    baseRequest(requestBuilder).map {
      _.as[Team].fold(
        code ⇒ failedRequest(s"Creating team $team failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def edit(id: Long, team: Team)(implicit ec: ExecutionContext): Future[Option[Team]] = {
    val url = s"${config.apiUrl}/teams/$id"
    val requestBuilder = new Request.Builder().url(url).patch(team.toJson)
    baseRequest(requestBuilder).map {
      _.as[Team].fold(
        code ⇒ failedRequest(s"Editing team $team failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def delete(id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/teams/$id"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting team $id failed", response.code(), false)
    }
  }
}
