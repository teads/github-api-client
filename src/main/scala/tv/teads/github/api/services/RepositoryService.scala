package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object RepositoryService extends GithubService with PayloadFormats {

  def listTags(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Tag]] = {
    import play.api.data.mapping.json.Rules._
    val route = s"repos/${configuration.organization}/$repository/tags"
    val errorMsg = s"Fetching tags for repository $repository failed"
    fetchMultiple[Tag](route, errorMsg)
  }

  def fetchAllRepositories(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[Repository](s"${configuration.api.url}/orgs/${configuration.organization}/repos", Map.empty)
  }

}
