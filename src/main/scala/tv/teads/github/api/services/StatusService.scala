package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._

class StatusService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  /**
   * @see https://developer.github.com/v3/repos/statuses/#create-a-status
   * @param repository
   * @param sha
   * @param status
   * @param ec
   * @return
   */
  def create(repository: String, sha: String, status: Status)(implicit ec: ExecutionContext): Future[Option[StatusResponse]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/statuses/$sha"
    val requestBuilder = new Request.Builder().url(url).post(status.toJson)
    baseRequest(requestBuilder).map {
      _.as[StatusResponse].fold(
        code ⇒ failedRequest(s"Creating $status on repository $repository for sha $sha failed", code, None),
        decoded ⇒ Some(decoded.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/statuses/#list-statuses-for-a-specific-ref
   * @param repository
   * @param sha
   * @param ec
   * @return
   */
  def list(repository: String, sha: String)(implicit ec: ExecutionContext): Future[List[Status]] =
    fetchMultiple[Status](
      s"repos/${config.owner}/$repository/commits/$sha/statuses",
      s"Fetching statuses for sha $sha failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/statuses/#get-the-combined-status-for-a-specific-ref
   * @param repository
   * @param sha
   * @param ec
   * @return
   */
  def get(repository: String, sha: String)(implicit ec: ExecutionContext): Future[Option[CombinedStatus]] =
    fetchOptional[CombinedStatus](
      s"repos/${config.owner}/$repository/commits/$sha/status",
      s"Fetching status for sha $sha failed"
    )
}
