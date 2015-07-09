package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import tv.teads.github.api.models._
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object RepositoryService extends GithubService {

  def fetchFile(repository: String, path: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/contents/$path"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          Some(response.entity.asString)

        case response ⇒
          logger.error(s"fetchFile with url $url failed with status code ${response.status.intValue}")
          None
      }
  }

  def listTags(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Tag]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/tags"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty).executeRequestInto[List[Tag]]().map {
      case SuccessfulRequest(tags, _) ⇒ tags
      case FailedRequest(statusCode) ⇒
        logger.error(s"Fetching tags for repository $repository failed, status code: ${statusCode.intValue}")
        Nil
    }
  }

  def fetchAllRepositories(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[Repository](s"${configuration.api.url}/orgs/${configuration.organization}/repos", Map.empty)
  }

}
