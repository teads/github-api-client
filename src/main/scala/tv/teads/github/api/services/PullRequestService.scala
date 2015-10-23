package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import play.api.data.mapping.Write
import play.api.libs.json.{JsObject, JsValue}
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.filters.common.Directions.Direction
import tv.teads.github.api.filters.common.States.State
import tv.teads.github.api.models._
import tv.teads.github.api.models.common.ADTEnum
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.{ExecutionContext, Future}

object PullRequestService extends GithubService with PayloadFormats {

  implicit lazy val pullRequestBranchParamJsonWrite: Write[PullRequestBranchParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestBranchParam, JsObject]
  }
  implicit lazy val pullRequestIssueParamJsonWrite: Write[PullRequestIssueParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestIssueParam, JsObject]
  }

  case class PullRequestBranchParam(
    title: String,
    head:  Head,
    base:  String,
    body:  Option[String] = None
  )

  case class PullRequestIssueParam(
    issue: Int,
    head:  Head,
    base:  String
  )

  def createFromBranch(org: String, repository: String, param: PullRequestBranchParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/$org/$repository/pulls"
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

  def createFromBranch(repository: String, param: PullRequestBranchParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    createFromBranch(configuration.organization, repository, param)
  }

  def createFromIssue(org: String, repository: String, param: PullRequestIssueParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/$org/$repository/pulls"
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
    createFromIssue(configuration.organization, repository, param)
  }

  case class PullRequestEditParam(
    title: Option[String] = None,
    body:  Option[String] = None,
    state: Option[State]  = None
  )
  implicit lazy val pullRequestEditParamJsonWrite: Write[PullRequestEditParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestEditParam, JsObject]
  }

  def edit(org: String, repository: String, number: Int, param: PullRequestEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.url}/repos/$org/$repository/pulls/$number"
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

  def edit(repository: String, number: Int, param: PullRequestEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    edit(configuration.organization, repository, number, param)
  }

  sealed trait Sort

  object Sort extends ADTEnum[Sort] {

    case object created extends Sort
    case object updated extends Sort
    case object popularity extends Sort
    case object `long-running` extends Sort

    val list = Seq(
      created, updated, popularity, `long-running`
    )
  }

  case class Head(author: String, branch: String) {
    override def toString = s"$author:$branch"
  }

  implicit lazy val _headJsonWrite: Write[Head, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Head, JsObject]
  }

  case class PullRequestFilter(
    state:     Option[State]     = Some(State.open),
    head:      Option[Head]      = None,
    base:      Option[String]    = None,
    sort:      Option[Sort]      = Some(Sort.created),
    direction: Option[Direction] = Some(Direction.desc)

  )

  def fetchPullRequests(org: String, repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[PullRequest]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[PullRequest](s"${configuration.url}/repos/$org/$repository/pulls", filter.toMapStringified)
  }
  def fetchPullRequests(repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[PullRequest]] = {
    fetchPullRequests(configuration.organization, repository, filter)
  }

  def fetchOpenPullRequests(org: String, repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(org, repository, filter.copy(state = Some(State.open)))

  def fetchOpenPullRequests(repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(configuration.organization, repository, filter.copy(state = Some(State.open)))

  def fetchClosedPullRequests(org: String, repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(org, repository, filter.copy(state = Some(State.closed)))

  def fetchClosedPullRequests(repository: String, filter: PullRequestFilter)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchPullRequests(configuration.organization, repository, filter.copy(state = Some(State.closed)))

  def fetchMatchingOpenPullRequests(org: String, repository: String, author: String, branch: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchOpenPullRequests(org, repository, PullRequestFilter(head = Some(Head(author, branch))))

  def fetchMatchingOpenPullRequests(repository: String, author: String, branch: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext) =
    fetchOpenPullRequests(configuration.organization, repository, PullRequestFilter(head = Some(Head(author, branch))))

  def byRepositoryAndNumber(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"repos/$org/$repository/pulls/$number"
    fetchOptional[PullRequest](url, s"Could not fetch Pull Request #$number for repository $repository")
  }

  def byRepositoryAndNumber(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[PullRequest]] = {
    byRepositoryAndNumber(configuration.organization, repository, number)
  }

  def isMerged(org: String, repository: String, number: Int)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/pulls/$number/merge"
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

  def isMerged(repository: String, number: Int)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    isMerged(configuration.organization, repository, number)
  }

  case class ToMerge(commit_message: String, sha: String)

  implicit lazy val toMergeJsonWrite: Write[ToMerge, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[ToMerge, JsObject]
  }

  def merge(org: String, repository: String, number: Int, toMerge: ToMerge)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/pulls/$number/merge"
    val req: HttpRequest = Put(url, toMerge)
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

  def merge(repository: String, number: Int, toMerge: ToMerge)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    merge(configuration.organization, repository, number, toMerge)
  }

  def fetchFiles(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[File]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.url}/repos/$org/$repository/pulls/$number/files"
    fetchAllPages[File](url, Map.empty)
  }

  def fetchFiles(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[File]] = {
    fetchFiles(configuration.organization, repository, number)
  }

  def fetchCommits(org: String, repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[GHCommit]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.url}/repos/$org/$repository/pulls/$number/commits"
    fetchAllPages[GHCommit](url, Map.empty)
  }

  def fetchCommits(repository: String, number: Long)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[GHCommit]] = {
    fetchCommits(configuration.organization, repository, number)
  }
}
