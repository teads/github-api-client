package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{HttpHeaders, StatusCodes}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

class HookService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  def fetchOrgHooks(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](s"orgs/${config.owner}/hooks", s"Fetching hooks for organization ${config.owner} failed")

  def fetchRepoHooks(repo: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](s"repos/${config.owner}/$repo/hooks", s"Fetching hooks for repository $repo failed")

  def fetchOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](s"orgs/${config.owner}/hooks/$id", s"Fetching hook $id for organization ${config.owner} failed")

  def fetchRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](s"repos/${config.owner}/$repo/hooks/$id", s"Fetching hook $id for repository $repo failed")

  def deleteOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/hooks/$id"
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

  def deleteRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repo/hooks/$id"
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

  def pingOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/hooks/$id/pings"
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

  def pingRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repo/hooks/$id"
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
