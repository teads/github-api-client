package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import io.circe.generic.semiauto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._

object LabelService {
  implicit lazy val labelParamEncoder = deriveEncoder[LabelParam]
  case class LabelParam(name: String, color: String)
}
class LabelService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import LabelService._

  /**
   * @see https://developer.github.com/v3/issues/labels/#create-a-label
   * @param repository
   * @param label
   * @param ec
   * @return
   */
  def create(repository: String, label: LabelParam)(implicit ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels"
    val requestBuilder = new Request.Builder().url(url).post(label.toJson)
    baseRequest(requestBuilder).map {
      _.as[Label].fold(
        code ⇒ failedRequest(s"Creating label $label in repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#update-a-label
   * @param repository
   * @param name
   * @param label
   * @param ec
   * @return
   */
  def update(repository: String, name: String, label: LabelParam)(implicit ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$name"
    val requestBuilder = new Request.Builder().url(url).patch(label.toJson)
    baseRequest(requestBuilder).map {
      _.as[Label].fold(
        code ⇒ failedRequest(s"Editing label $name in repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#delete-a-label
   * @param repository
   * @param label
   * @param ec
   * @return
   */
  def delete(repository: String, label: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$label"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting label $label for repository $repository failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#list-all-labels-for-this-repository
   * @param repository
   * @param ec
   * @return
   */
  def list(repository: String)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/labels",
      s"Fetching labels for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/labels/#get-a-single-label
   * @param repository
   * @param name
   * @param ec
   * @return
   */
  def get(repository: String, name: String)(implicit ec: ExecutionContext): Future[Option[Label]] =
    fetchOptional[Label](
      s"repos/${config.owner}/$repository/labels/$name",
      s"Fetching label $name for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/labels/#list-labels-on-an-issue
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def listOnIssue(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/issues/$number/labels",
      s"Fetching labels for issue #$number and repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/labels/#add-labels-to-an-issue
   * @param repository
   * @param number
   * @param labels
   * @param ec
   * @return
   */
  def addLabelsToIssue(repository: String, number: Long, labels: List[String])(implicit ec: ExecutionContext): Future[List[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val requestBuilder = new Request.Builder().url(url).post(labels.toJson)
    baseRequest(requestBuilder).map {
      _.as[List[Label]].fold(
        code ⇒ failedRequest(s"Adding labels $labels to issue #$number in repository $repository failed", code, Nil),
        _.decoded
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#remove-a-label-from-an-issue
   * @param repository
   * @param number
   * @param label
   * @param ec
   * @return
   */
  def removeLabelFromIssue(repository: String, number: Long, label: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels/$label"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing label $label from issue #$number in repository $repository failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#replace-all-labels-for-an-issue
   * @param repository
   * @param number
   * @param labels
   * @param ec
   * @return
   */
  def replaceLabelsForIssue(repository: String, number: Long, labels: List[String])(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val requestBuilder = new Request.Builder().url(url).put(labels.toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Replacing labels on issue #$number in repository $repository with $labels failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/labels/#remove-all-labels-from-an-issue
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def removeAllLabelsFromIssue(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing all labels from issue #$number in repository $repository failed", response.code(), false)
    }
  }

  /**
   * https://developer.github.com/v3/issues/labels/#get-labels-for-every-issue-in-a-milestone
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def listInMilestone(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/milestones/$number/labels",
      s"Fetching labels for milestone v$number in repository $repository failed"
    )
}
