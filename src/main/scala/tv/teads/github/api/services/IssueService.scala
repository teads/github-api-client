package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import play.api.data.mapping.Write
import play.api.libs.json.{JsObject, JsValue}
import spray.http._
import spray.http.HttpRequest
import spray.httpx.RequestBuilding._
import tv.teads.github.api.filters.common.Directions.Direction
import tv.teads.github.api.filters.common.States.State
import tv.teads.github.api.models._
import tv.teads.github.api.filters.common.Filter
import tv.teads.github.api.models.common.ADTEnum
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import shapeless._
import record._
import syntax.singleton._

import scala.concurrent.{ExecutionContext, Future}

object IssueService extends GithubService {

  implicit lazy val issueParamJsonWrite: Write[IssueParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[IssueParam, JsObject]
  }

  case class IssueParam(title: String,
                        body: Option[String] = None,
                        assignee: Option[String] = None,
                        state: Option[State] = None,
                        milestone: Option[String] = None,
                        labels: Set[String] = Set.empty)

  def create(repository: String, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/issues"
    val req: HttpRequest = Post(url, issue)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[Issue]()
      .map {
      case SuccessfulRequest(i, _) ⇒ Some(i)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not create issue, failed with status code ${statusCode.intValue}")
        None
    }
  }

  def edit(repository: String, number:Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/issues/$number"
    val req: HttpRequest = Patch(url, issue)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[Issue]()
      .map {
      case SuccessfulRequest(i, _) ⇒ Some(i)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not edit issue, failed with status code ${statusCode.intValue}")
        None
    }
  }

  def close(repository: String, number:Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    edit(repository, number, issue.copy(state = Some(State.closed)))
  }

  def open(repository: String, number:Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    edit(repository, number, issue.copy(state = Some(State.open)))
  }

  sealed trait Sort
  object Sort extends ADTEnum[Sort] {

    case object created  extends Sort
    case object updated  extends Sort
    case object comments extends Sort

    val list = Seq(
      created, updated, comments
    )
  }

  case class IssueFilter(milestone: Option[String] = None,
                         state: Option[State] = Some(State.open),
                         assignee: Option[String] = None,
                         creator: Option[String] = None,
                         mentioned: Option[String] = None,
                         labels: Set[String] = Set.empty,
                         sort: Option[Sort] = Some(Sort.created),
                         direction: Option[Direction] = Some(Direction.desc),
                         since: Option[DateTime] = None

                          ) extends Filter

  val filterGen = LabelledGeneric[IssueFilter]

  def filterToMap(issueFilter: IssueFilter) : Map[String, String] = {
    val hlist = filterGen.to(issueFilter)
    hlist.toMap.collect { case (k, v) =>
      v match {
        case it: Iterable[_] => k -> it
        case opt: Option[_] => k -> (opt: Iterable[_])
      }
    }.collect {
      case (k, v) if !v.isEmpty => k.name -> v.mkString(",")
    }
  }

  def byRepository(repository: String, issueFilter: IssueFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Issue]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/issues"
    val req: HttpRequest = Get(url)

    baseRequest(req, filterToMap(issueFilter))
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[List[Issue]]()
      .map {
      case SuccessfulRequest(i, _) ⇒ i
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could retrieve issues, failed with status code ${statusCode.intValue}")
        List.empty
    }
  }

  def byRepositoryAndNumber(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/issues/$number"
    val req: HttpRequest = Get(url)

    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[Issue]()
      .map {
      case SuccessfulRequest(i, _) ⇒ Some(i)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not retrieve issue, failed with status code ${statusCode.intValue}")
        None
    }
  }
}
