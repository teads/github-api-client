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

  def edit(repository: String, name: String, label: LabelParam)(implicit ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$name"
    val requestBuilder = new Request.Builder().url(url).patch(label.toJson)
    baseRequest(requestBuilder).map {
      _.as[Label].fold(
        code ⇒ failedRequest(s"Editing label $name in repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def delete(repository: String, label: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$label"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting label $label for repository $repository failed", response.code(), false)
    }
  }

  def fetchLabelsByRepo(repository: String)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/labels",
      s"Fetching labels for repository $repository failed"
    )

  def fetchLabel(repository: String, name: String)(implicit ec: ExecutionContext): Future[Option[Label]] =
    fetchOptional[Label](
      s"repos/${config.owner}/$repository/labels/$name",
      s"Fetching label $name for repository $repository failed"
    )

  def fetchLabelsByIssue(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/issues/$number/labels",
      s"Fetching labels for issue #$number and repository $repository failed"
    )

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

  def removeLabelFromIssue(repository: String, number: Long, label: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels/$label"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing label $label from issue #$number in repository $repository failed", response.code(), false)
    }
  }

  def replaceLabelsFromIssue(repository: String, number: Long, labels: List[String])(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val requestBuilder = new Request.Builder().url(url).put(labels.toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Replacing labels on issue #$number in repository $repository with $labels failed", response.code(), false)
    }
  }

  def removeAllLabelsFromIssue(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Removing all labels from issue #$number in repository $repository failed", response.code(), false)
    }
  }

  def fetchLabelsByMilestone(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/milestones/$number/labels",
      s"Fetching labels for milestone v$number in repository $repository failed"
    )
}
