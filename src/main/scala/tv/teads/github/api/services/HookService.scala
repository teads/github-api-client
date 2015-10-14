package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http.{HttpHeaders, HttpRequest, StatusCodes}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats

import scala.concurrent.{ExecutionContext, Future}

object HookService extends GithubService with PayloadFormats {

  def fetchOrgHooks(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"orgs/$org/hooks"
    val errorMsg = s"Fetching hooks for organization $org failed"
    fetchMultiple[Hook](url, errorMsg)
  }

  def fetchDefaultOrgHooks(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] = {
    fetchOrgHooks(configuration.organization)
  }

  def fetchRepoHooks(repo: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Hook]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"repos/${configuration.organization}/$repo/hooks"
    val errorMsg = s"Fetching hooks for repository $repo failed"
    fetchMultiple[Hook](url, errorMsg)
  }

  def fetchOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"orgs/$org/hooks/$id"
    val errorMsg = s"Fetching hook $id for organization $org failed"
    fetchOptional[Hook](url, errorMsg)
  }

  def fetchDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] = {
    fetchOrgHook(configuration.organization, id)
  }

  def fetchRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Hook]] = {
    import play.api.data.mapping.json.Rules._
    val url: String = s"repos/${configuration.organization}/$repo/hooks/$id"
    val errorMsg = s"Fetching hook $id for repository $repo failed"
    fetchOptional[Hook](url, errorMsg)
  }

  def deleteOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/orgs/$org/hooks/$id"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒
          true

        case response ⇒
          logger.error(s"Cannot delete hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def deleteDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    deleteOrgHook(configuration.organization, id)
  }

  def deleteRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repo/hooks/$id"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒
          true

        case response ⇒
          logger.error(s"Cannot delete hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def pingOrgHook(org: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/orgs/$org/hooks/$id/pings"
    val req: HttpRequest = Post(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒
          true

        case response ⇒
          logger.error(s"Failed to ping hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def pingDefaultOrgHook(id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    pingOrgHook(configuration.organization, id)
  }

  def pingRepoHook(repo: String, id: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repo/hooks/$id"
    val req: HttpRequest = Post(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒
          true

        case response ⇒
          logger.error(s"Failed to ping hook $id with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

}
