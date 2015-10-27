package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import io.circe.generic.semiauto._
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.filters._
import tv.teads.github.api.model._
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.{ExecutionContext, Future}

object PullRequestService extends GithubService with GithubApiCodecs {

  implicit lazy val _headEncoder = deriveFor[Head].encoder
  implicit lazy val pullRequestBranchParamEncoder = deriveFor[PullRequestBranchParam].encoder
  implicit lazy val pullRequestIssueParamEncoder = deriveFor[PullRequestIssueParam].encoder
  implicit lazy val pullRequestEditParamEncoder = deriveFor[PullRequestEditParam].encoder

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

  def createFromBranch(repository: String, param: PullRequestBranchParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/pulls"
    val req: HttpRequest = Post(url, param)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[PullRequest]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create issue, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def createFromIssue(repository: String, param: PullRequestIssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/pulls"
    val req: HttpRequest = Post(url, param)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[PullRequest]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not create issue, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def edit(repository: String, number: Int, param: PullRequestEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/pulls/$number"
    val req: HttpRequest = Patch(url, param)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[PullRequest]()
      .map {
        case SuccessfulRequest(i, _) ⇒ Some(i)
        case FailedRequest(statusCode) ⇒
          logger.error(s"Could not edit pull request, failed with status code ${statusCode.intValue}")
          None
      }
  }

  def fetchPullRequests(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[PullRequest]] =
    fetchAllPages[PullRequest](s"${configuration.url}/repos/${configuration.organization}/$repository/pulls", filter.toMapStringified)

  def fetchOpenPullRequests(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(repository, filter.copy(state = Some(IssueState.open)))

  def fetchClosedPullRequests(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(repository, filter.copy(state = Some(IssueState.closed)))

  def fetchMatchingOpenPullRequests(repository: String, author: String, branch: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchOpenPullRequests(repository, PullRequestFilter(head = Some(Head(author, branch))))

  def byRepositoryAndNumber(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] =
    fetchOptional[PullRequest](s"repos/${configuration.organization}/$repository/pulls/$number", s"Could not fetch Pull Request #$number for repository $repository")

  def isMerged(repository: String, number: Int)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/pulls/$number/merge"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.NoContent ⇒ true
        case response if response.status == StatusCodes.NotFound  ⇒ false
        case response ⇒
          logger.error(s"Could not retrieve pull request merged status, failed with status code ${response.status}")
          false

      }
  }

  def merge(repository: String, number: Int, commitMessage: String, sha: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/${configuration.organization}/$repository/pulls/$number/merge"
    val req: HttpRequest = Put(url, Map("commit_message" → commitMessage, "sha" → sha))
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK               ⇒ true
        case response if response.status == StatusCodes.MethodNotAllowed ⇒ false
        case response if response.status == StatusCodes.Conflict         ⇒ false
        case response ⇒
          logger.error(s"Could not merge pull request, failed with status code ${response.status}")
          false

      }
  }

  def fetchFiles(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[File]] =
    fetchAllPages[File](s"${configuration.url}/repos/${configuration.organization}/$repository/pulls/$number/files", Map.empty)

  def fetchCommits(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[GHCommit]] =
    fetchAllPages[GHCommit](s"${configuration.url}/repos/${configuration.organization}/$repository/pulls/$number/commits", Map.empty)
}
