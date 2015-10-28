package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

class RepositoryService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  def listTags(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Tag]] =
    fetchMultiple[Tag](s"repos/${config.owner}/$repository/tags", s"Fetching tags for repository $repository failed")

  def fetchAllRepositories(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Repository]] =
    fetchAllPages[Repository](s"${config.apiUrl}/orgs/${config.owner}/repos", Map.empty)

}
