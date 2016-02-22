package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

class HookService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  /**
   * @see https://developer.github.com/v3/orgs/hooks/#list-hooks
   * @param ec
   * @return
   */
  def listByOrganization(implicit ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](
      s"orgs/${config.owner}/hooks",
      s"Fetching hooks for organization ${config.owner} failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/hooks/#list-hooks
   * @param repository
   * @param ec
   * @return
   */
  def listByRepository(repository: String)(implicit ec: ExecutionContext): Future[List[Hook]] =
    fetchMultiple[Hook](
      s"repos/${config.owner}/$repository/hooks",
      s"Fetching hooks for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/orgs/hooks/#get-single-hook
   * @param id
   * @param ec
   * @return
   */
  def getByOrganization(id: Long)(implicit ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](
      s"orgs/${config.owner}/hooks/$id",
      s"Fetching hook $id for organization ${config.owner} failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/hooks/#get-single-hook
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def getByRepository(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Option[Hook]] =
    fetchOptional[Hook](
      s"repos/${config.owner}/$repository/hooks/$id",
      s"Fetching hook $id for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/orgs/hooks/#delete-a-hook
   * @param id
   * @param ec
   * @return
   */
  def deleteOrganizationHook(id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/hooks/$id"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting hook $id organization ${config.owner} failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/hooks/#delete-a-hook
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def deleteRepositoryHook(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/hooks/$id"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting hook $id for repository $repository failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/orgs/hooks/#ping-a-hook
   * @param id
   * @param ec
   * @return
   */
  def pingOrganizationHook(id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/hooks/$id/pings"
    val requestBuilder = new Request.Builder().url(url).post(emptyRequestBody)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Pinging hook $id for organization ${config.owner} failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/hooks/#ping-a-hook
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def pingRepositoryHook(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/hooks/$id"
    val requestBuilder = new Request.Builder().url(url).post(emptyRequestBody)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Pinging hook $id for repository $repository failed", response.code(), false)
    }
  }
}
