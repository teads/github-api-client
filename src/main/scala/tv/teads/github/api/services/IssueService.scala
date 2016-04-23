package tv.teads.github.api.services

import java.time.ZonedDateTime

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.Request
import io.circe.generic.semiauto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.filters._
import tv.teads.github.api.http._
import tv.teads.github.api.model._
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

object IssueService {
  implicit lazy val issueParamEncoder = deriveEncoder[IssueParam]

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

  /**
   * @see https://developer.github.com/v3/issues/#create-an-issue
   * @param repository
   * @param issue
   * @param ec
   * @return
   */
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

  /**
   * @see https://developer.github.com/v3/issues/#edit-an-issue
   * @param repository
   * @param number
   * @param issue
   * @param ec
   * @return
   */
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

  /**
   * @see https://developer.github.com/v3/issues/comments/#create-a-comment
   * @param repository
   * @param issueNumber
   * @param comment
   * @param ec
   * @return
   */
  def createComment(repository: String, issueNumber: Long, comment: String)(implicit ec: ExecutionContext): Future[Option[Comment]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$issueNumber/comments"
    val requestBuilder = new Request.Builder().url(url).post(Map("body" → comment).toJson)
    baseRequest(requestBuilder).map {
      _.as[Comment].fold(
        code ⇒ failedRequest(s"Commenting on issue #$issueNumber for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/comments/#edit-a-comment
   * @param repository
   * @param issueNumber
   * @param commentId
   * @param comment
   * @param ec
   * @return
   */
  def updateComment(repository: String, issueNumber: Long, commentId: Long, comment: String)(implicit ec: ExecutionContext): Future[Option[Comment]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/issues/$issueNumber/comments/$commentId"
    val requestBuilder = new Request.Builder().url(url).patch(Map("body" → comment).toJson)
    baseRequest(requestBuilder).map {
      _.as[Comment].fold(
        code ⇒ failedRequest(s"Editing comment #$commentId on issue #$issueNumber for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/issues/#edit-an-issue
   * @param repository
   * @param number
   * @param issue
   * @param ec
   * @return
   */
  def close(repository: String, number: Long, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.closed)))

  /**
   * @see https://developer.github.com/v3/issues/#edit-an-issue
   * @param repository
   * @param number
   * @param issue
   * @param ec
   * @return
   */
  def open(repository: String, number: Long, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    edit(repository, number, issue.copy(state = Some(IssueState.open)))

  /**
   * @see https://developer.github.com/v3/issues/#list-issues
   * @param repository
   * @param issueFilter
   * @param ec
   * @return
   */
  def list(repository: String, issueFilter: IssueFilter)(implicit ec: ExecutionContext): Future[List[Issue]] =
    fetchMultiple[Issue](
      s"repos/${config.owner}/$repository/issues",
      s"Fetching issues for repository $repository failed",
      issueFilter.toMapStringified
    )

  /**
   * @see https://developer.github.com/v3/issues/#get-a-single-issue
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def get(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Option[Issue]] =
    fetchOptional[Issue](
      s"repos/${config.owner}/$repository/issues/$number",
      s"Fetching issue #$number for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/comments/#get-a-single-comment
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def getComment(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Option[Comment]] =
    fetchOptional[Comment](
      s"repos/${config.owner}/$repository/issues/comments/$number",
      s"Fetching issue #$number for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/comments/#list-comments-on-an-issue
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def listIssueComments(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[Comment]] =
    fetchMultiple[Comment](
      s"repos/${config.owner}/$repository/issues/$number/comments",
      s"Fetching issue #$number comments for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/issues/comments/#list-comments-in-a-repository
   * @param repository
   * @param ec
   * @return
   */
  def listComments(repository: String)(implicit ec: ExecutionContext): Future[List[Comment]] =
    fetchMultiple[Comment](
      s"repos/${config.owner}/$repository/issues/comments",
      s"Fetching issues comments for repository $repository failed"
    )
}
