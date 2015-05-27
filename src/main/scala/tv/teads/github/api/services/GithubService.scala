package tv.teads.github.api.services


import spray.http._
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.util._
import tv.teads.github.api.services.GithubConfiguration.configuration

import scala.concurrent.{ExecutionContext, Future}

object GithubService extends Service with PayloadFormats {

  private val DefaultMediaType = "application/vnd.github.v3+json"
  private val TestMediaType = "application/vnd.github.moondragon+json"
  private val RawContentMediaType = "application/vnd.github.v3.raw"
  private val PagesNavRegex = """(?:\s*)<(.+)>; rel=(.+)""".r

  private def baseRequest(path: String,
                          queryParams: Map[String, String],
                          useTestMediaType: Boolean = false,
                          paginated: Boolean = false) = {
    val uri = Uri(path)
    val paramsWithToken = queryParams + configuration.api.tokenHeader
    val fullParams = if (paginated) paramsWithToken + configuration.api.paginationHeader else paramsWithToken
    val mediaType = if (useTestMediaType) TestMediaType else DefaultMediaType

    HttpClient(uri.withQuery(fullParams ++ uri.query))
      .withHeader(HttpHeaders.Accept.name, mediaType)
  }

  def fetchFile(repository: String, path: String)(implicit ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/contents/$path"

    baseRequest(url, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
      case response if response.status == StatusCodes.OK ⇒
        Some(response.entity.asString)

      case response ⇒
        logger.error(s"fetchFile with url $url failed with status code ${response.status.intValue}")
        None
    }
  }

  def listTags(repository: String)(implicit ec: ExecutionContext): Future[List[Tag]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/tags"
    baseRequest(url, Map.empty).executeRequestInto[List[Tag]]().map {
      case SuccessfulRequest(tags, _) ⇒ tags
      case FailedRequest(statusCode) ⇒
        logger.error(s"Fetching tags for repository $repository failed, status code: ${statusCode.intValue}")
        Nil
    }
  }

  def fetchAllRepositories(implicit ec: ExecutionContext): Future[List[Repository]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[Repository](s"${configuration.api.url}/orgs/${configuration.organization}/repos", Map.empty)
  }

  def fetchPullRequests(repository: String, queryParams: Map[String, String] = Map.empty)(implicit ec: ExecutionContext): Future[List[PullRequest]] = {
    import play.api.data.mapping.json.Rules._
    fetchAllPages[PullRequest](s"${configuration.api.url}/repos/${configuration.organization}/$repository/pulls", queryParams)
  }

  def fetchOpenPullRequests(repository: String, queryParams: Map[String, String] = Map.empty)(implicit ec: ExecutionContext) =
    fetchPullRequests(repository, queryParams + ("state" -> "open"))

  def fetchMatchingOpenPullRequests(repository: String, author: String, branch: String)(implicit ec: ExecutionContext) =
    fetchOpenPullRequests(repository, Map("head" -> s"$author:$branch"))

  def getRateLimit(implicit ec: ExecutionContext): Future[RateLimit] =
    baseRequest(s"${configuration.api.url}/rate_limit", Map.empty)
      .executeRequestInto[RateLimit]().map {
      case SuccessfulRequest(rateLimit, _) ⇒ rateLimit
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not fetch rate limit, failed with status code ${statusCode.intValue}")
        val core = Core(0, 0, 0)
        RateLimit(Resources(core, core), core)
    }

  def fetchAllPages[T: FromResponseUnmarshaller](url: String, queryParams: Map[String, String])(implicit ec: ExecutionContext, ev: FromResponseUnmarshaller[List[T]]) = {

    def findNextPageUrl(linkHeader: Option[String]): Option[String] =
      linkHeader.flatMap { links ⇒
        links.split(",").collectFirst {
          case PagesNavRegex(link, rel) if rel == "next" ⇒ link
        }
      }
    def fetchAux(url: String, alreadyFetched: Future[List[T]])(implicit ec: ExecutionContext): Future[List[T]] = {
      baseRequest(url, queryParams, useTestMediaType = true, paginated = true)
        .executeRequestInto[List[T]]()
        .flatMap {
        case SuccessfulRequest(repositories, raw) ⇒
          val fetchedRepos = alreadyFetched.map(_ ++ repositories)

          findNextPageUrl(raw.headers.find(_.name == "Link").map(_.value))
            .map(nextUrl ⇒ fetchAux(nextUrl, fetchedRepos))
            .getOrElse(fetchedRepos)

        case FailedRequest(statusCode) ⇒
          logger.error(s"Failed to fetch repositories, request to $url completed with ${statusCode.intValue}")
          alreadyFetched
      }
    }

    fetchAux(url, Future.successful(Nil))
  }
}
