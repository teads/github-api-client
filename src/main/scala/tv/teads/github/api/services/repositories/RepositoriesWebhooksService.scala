package tv.teads.github.api.services.repositories

import io.circe.syntax._
import io.circe.generic.auto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.repositories.{CreateWebhookRequest, EditWebhookRequest, RepositoryWebhook}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class RepositoriesWebhooksService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAll(repository: String)(implicit ec: EC): Future[Option[List[RepositoryWebhook]]] =
    getAllPages[RepositoryWebhook](
      s"repos/${config.owner}/$repository/hooks",
      s"Could not fetch repository webhooks list for repository $repository"
    )

  def listSinglePage(repository: String, page: Int)(implicit ec: EC): Future[Option[List[RepositoryWebhook]]] =
    jsonOptionalIfFailed[List[RepositoryWebhook]](
      singlePageGetCall(s"repos/${config.owner}/$repository/hooks", page),
      s"Could not fetch page $page of repository webhooks list for repository $repository"
    )

  def get(repository: String, hookId: Int)(implicit ec: EC): Future[Option[RepositoryWebhook]] =
    jsonOptionalIfFailed[RepositoryWebhook](
      getCall(s"repos/${config.owner}/$repository/hooks/$hookId"),
      s"Could not fetch webhooks with id $hookId for repository $repository"
    )

  def create(repository: String, request: CreateWebhookRequest)(implicit ec: EC): Future[Option[RepositoryWebhook]] =
    jsonOptionalIfFailed[RepositoryWebhook](
      postCall(
        s"repos/${config.owner}/$repository/hooks",
        body = jsonRequestBody(request)
      ),
      s"Could not create webhook ${printJson(request)} for repository $repository"
    )

  def edit(repository: String, hookId: Int, request: EditWebhookRequest)(implicit ec: EC): Future[Option[RepositoryWebhook]] =
    jsonOptionalIfFailed[RepositoryWebhook](
      patchCall(
        s"repos/${config.owner}/$repository/hooks/$hookId",
        body = jsonRequestBody(request)
      ),
      s"Could not edit webhook $hookId with ${printJson(request)} for repository $repository"
    )

  def delete(repository: String, hookId: Int)(implicit ec: EC): Future[Boolean] =
    isSuccessful(
      deleteCall(s"repos/${config.owner}/$repository/hooks/$hookId")
    )

  def testPush(repository: String, hookId: Int)(implicit ec: EC): Future[Boolean] =
    isSuccessful(
      postCall(s"repos/${config.owner}/$repository/hooks/$hookId/tests")
    )

  def ping(repository: String, hookId: Int)(implicit ec: EC): Future[Boolean] =
    isSuccessful(
      postCall(s"repos/${config.owner}/$repository/hooks/$hookId/pings")
    )

}
