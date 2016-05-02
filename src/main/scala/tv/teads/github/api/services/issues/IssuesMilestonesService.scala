package tv.teads.github.api.services.issues

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.issues._
import tv.teads.github.api.services.AbstractGithubService
import tv.teads.github.api.util.ClassToMap._

import scala.concurrent.Future

class IssuesMilestonesService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAll(repository: String, parameters: ListMilestonesParameters = ListMilestonesParameters())(implicit ec: EC): Future[Option[List[Milestone]]] =
    jsonOptionalIfFailed[List[Milestone]](
      getCall(
        s"repos/${config.owner}/$repository/milestones",
        params = parameters.toStringMap
      ),
      s"Could not fetch milestone list for repository $repository"
    )

  def get(repository: String, milestoneNumber: Int)(implicit ec: EC): Future[Option[Milestone]] =
    jsonOptionalIfFailed[Milestone](
      getCall(s"repos/${config.owner}/$repository/milestones/$milestoneNumber"),
      s"Could not fetch milestone $milestoneNumber for repository $repository"
    )

  def create(repository: String, request: CreateMilestoneRequest)(implicit ec: EC): Future[Option[Milestone]] =
    jsonOptionalIfFailed[Milestone](
      postCall(
        s"repos/${config.owner}/$repository/milestones",
        body = jsonRequestBody(request)
      ),
      s"Could not create milestone ${printJson(request)} for repository $repository"
    )

  def edit(repository: String, milestoneNumber: Int, request: EditMilestoneRequest)(implicit ec: EC): Future[Option[Milestone]] =
    jsonOptionalIfFailed[Milestone](
      patchCall(
        s"repos/${config.owner}/$repository/milestones/$milestoneNumber",
        body = jsonRequestBody(request)
      ),
      s"Could not edit milestone $milestoneNumber with ${printJson(request)} for repository $repository"
    )

  def delete(repository: String, milestoneNumber: Int)(implicit ec: EC): Future[Boolean] =
    isSuccessful(
      deleteCall(s"repos/${config.owner}/$repository/milestones/$milestoneNumber")
    )

}
