package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import play.api.data.mapping.Write
import play.api.libs.json.{JsObject, JsValue}
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object LabelService extends GithubService with PayloadFormats {

  implicit lazy val labelParamJsonWrite: Write[LabelParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[LabelParam, JsObject]
  }

  case class LabelParam(
    name:  String,
    color: String
  )

  def create(org: String, repository: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"$org/repos/${configuration.organization}/$repository/labels"
    val req: HttpRequest = Post(url, label)
    baseRequest(req, Map.empty)
      .executeRequestInto[Label]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create label, failed with status code ${statusCode.intValue}")
          None
      }
  }
  def create(repository: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    create(configuration.organization, repository, label)
  }

  def edit(org: String, repository: String, name: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    val url = s"${configuration.url}/repos/$org/$repository/labels/$name"
    val req: HttpRequest = Patch(url, label)
    baseRequest(req, Map.empty)
      .executeRequestInto[Label]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not edit label $name, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def edit(repository: String, name: String, label: LabelParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    edit(configuration.organization, repository, name, label)
  }

  def delete(org: String, repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/labels/$name"
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
  def delete(repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    delete(configuration.organization, repository, name)
  }

  def fetchLabelsByRepo(org: String, repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"repos/$org/$repository/labels"
    val errorMsg = s"Fetching labels for repository $repository failed"
    fetchMultiple[Label](url, errorMsg)
  }

  def fetchLabelsByRepo(repository: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    fetchLabelsByRepo(configuration.organization, repository)
  }

  def fetchLabel(org: String, repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"repos/$org/$repository/labels/$name"
    val errorMsg = s"Fetching label $name for repository $repository failed"
    fetchOptional[Label](url, errorMsg)
  }

  def fetchLabel(repository: String, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Label]] = {
    fetchLabel(configuration.organization, repository, name)
  }

  def fetchLabelsByIssue(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"repos/$org/$repository/issues/$number/labels"
    val errorMsg = s"Fetching labels for issue #$number and repository $repository failed"
    fetchMultiple[Label](url, errorMsg)
  }

  def fetchLabelsByIssue(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    fetchLabelsByIssue(configuration.organization, repository, number)
  }

  def addLabelsToIssue(org: String, repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    import play.api.data.mapping.json.Rules._
    import play.api.data.mapping.json.Writes._
    val url = s"$org/repos/${configuration.organization}/$repository/issues/$number/labels"
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

  def addLabelsToIssue(repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    addLabelsToIssue(configuration.organization, number, labels)
  }

  def removeLabelFromIssue(org: String, repository: String, number: Long, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"$org/repos/${configuration.organization}/$repository/issues/$number/labels/$name"
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

  def removeLabelFromIssue(repository: String, number: Long, name: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    removeLabelFromIssue(configuration.organization, number, name)
  }

  def replaceLabelsFromIssue(org: String, repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    import play.api.data.mapping.json.Writes._
    val url = s"$org/repos/${configuration.organization}/$repository/issues/$number/labels"
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

  def replaceLabelsFromIssue(repository: String, number: Long, labels: List[String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    replaceLabelsFromIssue(configuration.organization, repository, number, labels)
  }

  def removeAllLabelsFromIssue(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"$org/repos/${configuration.organization}/$repository/issues/$number/labels"
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

  def removeLabelFromIssue(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    removeAllLabelsFromIssue(configuration.organization, repository, number)
  }

  def fetchLabelsByMilestone(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"repos/$org/$repository/milestones/$number/labels"
    val errorMsg = s"Fetching labels for milestone v$number and repository $repository failed"
    fetchMultiple[Label](url, errorMsg)
  }

  def fetchLabelsByMilestone(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Label]] = {
    fetchLabelsByMilestone(configuration.organization, repository, number)
  }

}
