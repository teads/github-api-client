package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{HttpHeaders, StatusCodes}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

object HookService extends GithubService with GithubApiCodecs {

  def fetchOrgHooks(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](s"orgs/$org/hooks", s"Fetching hooks for organization $org failed")

  def fetchDefaultOrgHooks(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] =
    fetchOrgHooks(configuration.organization)

  def fetchRepoHooks(repo: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](s"repos/${configuration.organization}/$repo/hooks", s"Fetching hooks for repository $repo failed")

  def fetchOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](s"orgs/$org/hooks/$id", s"Fetching hook $id for organization $org failed")

  def fetchDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] =
    fetchOrgHook(configuration.organization, id)

  def fetchRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](s"repos/${configuration.organization}/$repo/hooks/$id", s"Fetching hook $id for repository $repo failed")

  def deleteOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/orgs/$org/hooks/$id"
    baseRequest(Delete(url), Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Cannot delete hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def deleteDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] =
    deleteOrgHook(configuration.organization, id)

  def deleteRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repo/hooks/$id"
    baseRequest(Delete(url), Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Cannot delete hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def pingOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/orgs/$org/hooks/$id/pings"
    baseRequest(Post(url), Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Failed to ping hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def pingDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] =
    pingOrgHook(configuration.organization, id)

  def pingRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repo/hooks/$id"
    baseRequest(Post(url), Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Failed to ping hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

}
