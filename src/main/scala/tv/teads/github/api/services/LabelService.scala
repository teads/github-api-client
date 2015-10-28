package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import io.circe.generic.semiauto._
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object LabelService {
  implicit lazy val labelParamEncoder = deriveFor[LabelParam].encoder
  case class LabelParam(name: String, color: String)
}
class LabelService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import LabelService._

  def create(repository: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels"
    val req: HttpRequest = Post(url, label)
    baseRequest(req, Map.empty)
      .executeRequestInto[Label]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create label $label, url $url, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def edit(repository: String, name: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$name"
    val req: HttpRequest = Patch(url, label)
    baseRequest(req, Map.empty)
      .executeRequestInto[Label]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not edit label $name, url $url, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def delete(repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/labels/$name"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not delete label $name for repository $repository, failed with status code ${response.status}")
          false
      }
  }

  def fetchLabelsByRepo(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](s"repos/${config.owner}/$repository/labels", s"Fetching labels for repository $repository failed")

  def fetchLabel(repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] =
    fetchOptional[Label](s"repos/${config.owner}/$repository/labels/$name", s"Fetching label $name for repository $repository failed")

  def fetchLabelsByIssue(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](s"repos/${config.owner}/$repository/issues/$number/labels", s"Fetching labels for issue #$number and repository $repository failed")

  def addLabelsToIssue(repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val req: HttpRequest = Post(url, labels)
    baseRequest(req, Map.empty)
      .executeRequestInto[List[Label]]()
      .map {
        case SuccessfulRequest(i, _) ⇒ i
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not add labels to issue #$number, failed with status code ${statusCode.intValue}")
          Nil
      }
  }

  def removeLabelFromIssue(repository: String, number: Long, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels/$name"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove label $name from issue #$number on repository $repository, failed with status code ${response.status}")
          false
      }
  }

  def replaceLabelsFromIssue(repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val req: HttpRequest = Put(url, labels)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove labels $labels from issue #$number on repository $repository, failed with status code ${response.status}")
          false
      }
  }

  def removeAllLabelsFromIssue(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number/labels"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response ⇒
          logger.error(s"Could not remove all labels from issue #$number on repository $repository, failed with status code ${response.status}")
          false
      }
  }

  def fetchLabelsByMilestone(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] =
    fetchMultiple[Label](
      s"repos/${config.owner}/$repository/milestones/$number/labels",
      s"Fetching labels for milestone v$number and repository $repository failed"
    )
}
