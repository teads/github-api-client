package tv.teads.github.api.services.issues

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.common.GithubAccount
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class IssuesAssigneesService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAvailableAssignees(repository: String)(implicit ec: EC): Future[Option[List[GithubAccount]]] =
    jsonOptionalIfFailed[List[GithubAccount]](
      getCall(s"repos/${config.owner}/$repository/assignees"),
      s"Could not fetch list of available assignees for repository $repository"
    )

  def isAvailableAssignee(repository: String, username: String)(implicit ec: EC): Future[Boolean] =
    isSuccessful(
      getCall(s"repos/${config.owner}/$repository/assignees/$username")
    )

}
