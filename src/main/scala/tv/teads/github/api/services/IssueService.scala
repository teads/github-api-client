package tv.teads.github.api.services

import java.time.ZonedDateTime

import scala.concurrent.{ExecutionContext, Future}

import com.squareup.okhttp.Request
import io.circe.generic.semiauto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.filters._
import tv.teads.github.api.http._
import tv.teads.github.api.model._
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

object IssueService {
  implicit lazy val issueParamEncoder = deriveFor[IssueParam].encoder

  case class IssueParam(
    title:     String,
    body:      Option[String]     = None,
    assignee:  Option[String]     = None,
    state:     Option[IssueState] = None,
    milestone: Option[String]     = None,
    labels:    Set[String]        = Set.empty
  )

  sealed trait Sort
  object Sort extends Enumerated[Sort] {
    val values = List(created, updated, comments)

    case object created extends Sort
    case object updated extends Sort
    case object comments extends Sort
  }

  case class IssueFilter(
    milestone: Option[String]        = None,
    state:     Option[IssueState]    = Some(IssueState.open),
    assignee:  Option[String]        = None,
    creator:   Option[String]        = None,
    mentioned: Option[String]        = None,
    labels:    Set[String]           = Set.empty,
    sort:      Option[Sort]          = Some(Sort.created),
    direction: Option[Direction]     = Some(Direction.desc),
    since:     Option[ZonedDateTime] = None

  )
}
class IssueService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import IssueService._

  def create(repository: String, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues"
    val requestBuilder = new Request.Builder().url(url).post(issue.toJson)
    baseRequest(requestBuilder).map {
      _.as[Issue].fold(
        code ⇒ failedRequest(s"Creating issue $issue for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def edit(repository: String, number: Long, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$number"
    val requestBuilder = new Request.Builder().url(url).patch(issue.toJson)
    baseRequest(requestBuilder).map {
      _.as[Issue].fold(
        code ⇒ failedRequest(s"Editing issue $issue for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def close(repository: String, number: Long, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.closed)))

  def open(repository: String, number: Long, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.open)))

  def byRepository(repository: String, issueFilter: IssueFilter)(implicit ec: ExecutionContext): Future[List[Issue]] =
    fetchMultiple[Issue](
      s"repos/${config.owner}/$repository/issues",
      s"Fetching issues for repository $repository failed",
      issueFilter.toMapStringified
    )

  def byRepositoryAndNumber(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    fetchOptional[Issue](
      s"repos/${config.owner}/$repository/issues/$number",
      s"Fetching issue #$number for repository $repository failed"
    )
}
