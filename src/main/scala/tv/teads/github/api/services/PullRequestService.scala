package tv.teads.github.api.services

import play.api.data.mapping.Write
import play.api.libs.json.{JsObject, JsValue}
import spray.http.{HttpRequest, _}
import spray.httpx.RequestBuilding._
import tv.teads.github.api.filters.common.Directions.Direction
import tv.teads.github.api.filters.common.Filter
import tv.teads.github.api.filters.common.States.State
import tv.teads.github.api.models._
import tv.teads.github.api.models.common.ADTEnum
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import shapeless._
import record._
import syntax.singleton._

import scala.concurrent.{ExecutionContext, Future}

object PullRequestService extends GithubService {

  implicit lazy val pullRequestBranchParamJsonWrite: Write[PullRequestBranchParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestBranchParam, JsObject]
  }
  implicit lazy val pullRequestIssueParamJsonWrite: Write[PullRequestIssueParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestIssueParam, JsObject]
  }

  case class PullRequestBranchParam(title: String,
                                    head: Head,
                                    base: String,
                                    body: Option[String] = None)

  case class PullRequestIssueParam(issue: Int,
                                   head: Head,
                                   base: String)

  def createFromBranch(repository: String, param: PullRequestBranchParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls"
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

  def createFromIssue(repository: String, param: PullRequestIssueParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls"
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

  case class PullRequestEditParam(title: Option[String] = None,
                                  body: Option[String] = None,
                                  state: Option[State] = None)
  implicit lazy val pullRequestEditParamJsonWrite: Write[PullRequestEditParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestEditParam, JsObject]
  }

  def edit(repository: String, number:Int, param: PullRequestEditParam)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number"
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

  case class PullRequestFilter(state: Option[State] = Some(State.open),
                               head: Option[Head] = None,
                               base: Option[String] = None,
                               sort: Option[Sort] = Some(Sort.created),
                               direction: Option[Direction] = Some(Direction.desc)

                                ) extends Filter

  val filterGen = LabelledGeneric[PullRequestFilter]

  def filterToMap(filter: PullRequestFilter): Map[String, String] = {
    val hlist = filterGen.to(filter)
    hlist.toMap.collect { case (k, v) =>
      v match {
        case it: Iterable[_] => k -> it
        case opt: Option[_] => k -> (opt: Iterable[_])
      }
    }.collect {
      case (k, v) if !v.isEmpty => k.name -> v.mkString(",")
    }
  }

  def fetchPullRequests(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit ec: ExecutionContext): Future[List[PullRequest]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[PullRequest](s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls", filterToMap(filter))
  }

  def fetchOpenPullRequests(repository: String, filter: PullRequestFilter = PullRequestFilter())(implicit ec: ExecutionContext) =
    fetchPullRequests(repository, filter.copy(state = Some(State.open)))

  def fetchMatchingOpenPullRequests(repository: String, author: String, branch: String)(implicit ec: ExecutionContext) =
    fetchOpenPullRequests(repository, PullRequestFilter(head = Some(Head(author, branch))))

  def byRepositoryAndNumber(repository: String, number: Long)(implicit ec: ExecutionContext): Future[Option[PullRequest]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number"
    val req: HttpRequest = Get(url)

    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequestInto[PullRequest]()
      .map {
      case SuccessfulRequest(i, _) ⇒ Some(i)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not retrieve pull equest, failed with status code ${statusCode.intValue}")
        None
    }
  }

  def isMerged(repository: String, number:Int)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number/merge"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
      case response if response.status == StatusCodes.NoContent ⇒ true
      case response if response.status == StatusCodes.NotFound ⇒ false
      case response  ⇒  logger.error(s"Could not retrieve pull request merged status, failed with status code ${response.status}")
        false

    }
  }

  case class ToMerge(commit_message: String, sha: String)

  implicit lazy val toMergeJsonWrite: Write[ToMerge, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[ToMerge, JsObject]
  }

  def merge(repository: String, number:Int, toMerge:ToMerge)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number/merge"
    val req: HttpRequest = Put(url, toMerge)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
      case response if response.status == StatusCodes.OK ⇒ true
      case response if response.status == StatusCodes.MethodNotAllowed ⇒ false
      case response if response.status == StatusCodes.Conflict ⇒ false
      case response  ⇒  logger.error(s"Could not merge pull request, failed with status code ${response.status}")
        false

    }
  }

  def fetchFiles(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[File]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number/files"
    fetchAllPages[File](url, Map.empty)
  }

  def fetchCommits(repository: String, number: Long)(implicit ec: ExecutionContext): Future[List[GHCommit]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls/$number/commits"
    fetchAllPages[GHCommit](url, Map.empty)
  }
}
