package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import io.circe.generic.semiauto._
import spray.http._
import spray.http.HttpRequest
import spray.httpx.RequestBuilding._
import tv.teads.github.api.filters._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.{ExecutionContext, Future}

object IssueService extends GithubService with GithubApiCodecs {

  implicit lazy val issueParamEncoder = deriveFor[IssueParam].encoder

  case class IssueParam(
    title:     String,
    body:      Option[String]     = None,
    assignee:  Option[String]     = None,
    state:     Option[IssueState] = None,
    milestone: Option[String]     = None,
    labels:    Set[String]        = Set.empty
  )

  def create(repository: String, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/issues"
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

  def edit(repository: String, number: Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/issues/$number"
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

  def close(repository: String, number: Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.closed)))

  def open(repository: String, number: Long, issue: IssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.open)))

  sealed trait Sort
  object Sort extends Enumerated[Sort] {
    val values = List(created, updated, comments)

    case object created extends Sort
    case object updated extends Sort
    case object comments extends Sort
  }

  case class IssueFilter(
    milestone: Option[String]     = None,
    state:     Option[IssueState] = Some(IssueState.open),
    assignee:  Option[String]     = None,
    creator:   Option[String]     = None,
    mentioned: Option[String]     = None,
    labels:    Set[String]        = Set.empty,
    sort:      Option[Sort]       = Some(Sort.created),
    direction: Option[Direction]  = Some(Direction.desc),
    since:     Option[DateTime]   = None

  )

  def byRepository(repository: String, issueFilter: IssueFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Issue]] =
    fetchMultiple[Issue](s"repos/${configuration.organization}/$repository/issues", s"Fetching issues for repository $repository failed", issueFilter.toMapStringified)

  def byRepositoryAndNumber(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Issue]] =
    fetchOptional[Issue](s"repos/${configuration.organization}/$repository/issues/$number", s"Fetching issue #$number for repository $repository failed")
}
