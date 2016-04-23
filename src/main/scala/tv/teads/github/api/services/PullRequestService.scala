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

object PullRequestService {
  implicit lazy val _headEncoder = deriveEncoder[Head]
  implicit lazy val pullRequestBranchParamEncoder = deriveEncoder[PullRequestBranchParam]
  implicit lazy val pullRequestIssueParamEncoder = deriveEncoder[PullRequestIssueParam]
  implicit lazy val pullRequestEditParamEncoder = deriveEncoder[PullRequestEditParam]

  case class Head(author: String, branch: String) {
    override def toString = s"$author:$branch"
  }

  case class PullRequestBranchParam(title: String, head: Head, base: String, body: Option[String] = None)
  case class PullRequestIssueParam(issue: Int, head: Head, base: String)
  case class PullRequestEditParam(title: Option[String] = None, body: Option[String] = None, state: Option[IssueState] = None)

  sealed trait Sort

  object Sort extends Enumerated[Sort] {
    val values = List(created, updated, popularity, `long-running`)

    case object created extends Sort
    case object updated extends Sort
    case object popularity extends Sort
    case object `long-running` extends Sort
  }

  case class PullRequestFilter(
    state:     Option[IssueState] = Some(IssueState.open),
    head:      Option[Head]       = None,
    base:      Option[String]     = None,
    sort:      Option[Sort]       = Some(Sort.created),
    direction: Option[Direction]  = Some(Direction.desc)
  )

  case class PullRequestCommentFilter(
    sort:      Option[Sort]          = Some(Sort.created),
    direction: Option[Direction]     = Some(Direction.desc),
    since:     Option[ZonedDateTime] = None
  )
}
class PullRequestService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import PullRequestService._

  /**
   * @see https://developer.github.com/v3/pulls/#create-a-pull-request
   * @param repository
   * @param param
   * @param ec
   * @return
   */
  def createFromBranch(repository: String, param: PullRequestBranchParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/pulls"
    val requestBuilder = new Request.Builder().url(url).post(param.toJson)
    baseRequest(requestBuilder).map {
      _.as[PullRequest].fold(
        code ⇒ failedRequest(s"Creating pull request from branch $param on repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/pulls/#create-a-pull-request
   * @param repository
   * @param param
   * @param ec
   * @return
   */
  def createFromIssue(repository: String, param: PullRequestIssueParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/pulls"
    val requestBuilder = new Request.Builder().url(url).post(param.toJson)
    baseRequest(requestBuilder).map {
      _.as[PullRequest].fold(
        code ⇒ failedRequest(s"Creating pull request from issue $param on repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/pulls/#update-a-pull-request
   * @param repository
   * @param number
   * @param param
   * @param ec
   * @return
   */
  def update(repository: String, number: Int, param: PullRequestEditParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/pulls/$number"
    val requestBuilder = new Request.Builder().url(url).patch(param.toJson)
    baseRequest(requestBuilder).map {
      _.as[PullRequest].fold(
        code ⇒ failedRequest(s"Editing pull request #$number on repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/pulls/#list-pull-requests
   * @param repository
   * @param filter
   * @param ec
   * @return
   */
  def list(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit ec: ExecutionContext): Future[List[PullRequest]] =
    fetchAllPages[PullRequest](
      s"repos/${config.owner}/$repository/pulls",
      s"Fetching pull requests on repository $repository using filter $filter failed",
      filter.toMapStringified
    )

  /**
   * @see https://developer.github.com/v3/pulls/#list-pull-requests
   * @param repository
   * @param filter
   * @param ec
   * @return
   */
  def listOpen(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit ec: ExecutionContext) =
    list(repository, filter.copy(state = Some(IssueState.open)))

  /**
   * @see https://developer.github.com/v3/pulls/#list-pull-requests
   * @param repository
   * @param filter
   * @param ec
   * @return
   */
  def listClosed(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit ec: ExecutionContext) =
    list(repository, filter.copy(state = Some(IssueState.closed)))

  /**
   * @see https://developer.github.com/v3/pulls/#get-a-single-pull-request
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def get(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Option[PullRequest]] =
    fetchOptional[PullRequest](
      s"repos/${config.owner}/$repository/pulls/$number",
      s"Fetching pull request #$number for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/pulls/#get-if-a-pull-request-has-been-merged
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def isMerged(repository: String, number: Int)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/pulls/$number/merge"
    val requestBuilder = new Request.Builder().url(url).get()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response if response.code() == 404 ⇒ false
      case response ⇒
        failedRequest(s"Retrieving pull request #$number on $repository merged status failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/pulls/#merge-a-pull-request-merge-button
   * @param repository
   * @param number
   * @param commitMessage
   * @param sha
   * @param ec
   * @return
   */
  def merge(repository: String, number: Int, commitMessage: String, sha: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/pulls/$number/merge"
    val requestBuilder = new Request.Builder().url(url).put(Map("commit_message" → commitMessage, "sha" → sha).toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response if response.code() == 404 ⇒ false
      case response if response.code() == 409 ⇒ false
      case response ⇒
        failedRequest(s"Merging pull request #$number on repository $repository failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/pulls/#list-pull-requests-files
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def listFiles(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[File]] =
    fetchAllPages[File](
      s"repos/${config.owner}/$repository/pulls/$number/files",
      s"Fetching pull request #$number files failed"
    )

  /**
   * @see https://developer.github.com/v3/pulls/#list-commits-on-a-pull-request
   * @param repository
   * @param number
   * @param ec
   * @return
   */
  def listCommits(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[GHCommit]] =
    fetchAllPages[GHCommit](
      s"repos/${config.owner}/$repository/pulls/$number/commits",
      s"Fetching pull request #$number commits failed"
    )

  /**
   * @see https://developer.github.com/v3/pulls/comments/#get-a-single-comment
   * @param repository
   * @param commentId
   * @param ec
   * @return
   */
  def getComment(repository: String, commentId: Long)(implicit ec: ExecutionContext): Future[Option[PullRequestReviewComment]] =
    fetchOptional[PullRequestReviewComment](
      s"repos/${config.owner}/$repository/pulls/comments/$commentId",
      s"Fetching pull request comment #$commentId for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/pulls/comments/#list-comments-in-a-repository
   * @param repository
   * @param pullRequestCommentFilter
   * @param ec
   * @return
   */
  def listComments(repository: String, pullRequestCommentFilter: PullRequestCommentFilter = PullRequestCommentFilter())(implicit ec: ExecutionContext): Future[List[PullRequestReviewComment]] =
    fetchMultiple[PullRequestReviewComment](
      s"repos/${config.owner}/$repository/pulls/comments",
      s"Fetching pull request comments for repository $repository failed",
      pullRequestCommentFilter.toMapStringified
    )
}
