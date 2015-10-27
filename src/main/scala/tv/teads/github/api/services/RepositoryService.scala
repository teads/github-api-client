package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

object RepositoryService extends GithubService with GithubApiCodecs {

  def listTags(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Tag]] =
    fetchMultiple[Tag](s"repos/${configuration.organization}/$repository/tags", s"Fetching tags for repository $repository failed")

  def fetchAllRepositories(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] =
    fetchAllPages[Repository](s"${configuration.url}/orgs/${configuration.organization}/repos", Map.empty)

}
