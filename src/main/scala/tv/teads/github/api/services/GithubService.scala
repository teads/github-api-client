package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import com.typesafe.scalalogging.LazyLogging
import spray.http._
import spray.httpx.RequestBuilding._
import spray.httpx.unmarshalling._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.json.CirceSupport
import tv.teads.github.api.util._

import scala.concurrent.{Future, ExecutionContext}

abstract class GithubService(config: GithubApiClientConfig) extends LazyLogging with CirceSupport {

  private type FRU[T] = FromResponseUnmarshaller[T]

  protected val DefaultMediaType = "application/vnd.github.v3+json"
  protected val TestMediaType = "application/vnd.github.moondragon+json"
  protected val PermissionMediaType = "application/vnd.github.ironman-preview+json"
  protected val RawContentMediaType = "application/vnd.github.v3.raw"
  protected val PagesNavRegex = """(?:\s*)<(.+)>; rel=(.+)""".r

  protected def baseRequest(
    req:                    HttpRequest,
    queryParams:            Map[String, String],
    token:                  Option[String]      = None,
    useTestMediaType:       Boolean             = false,
    usePermissionMediaType: Boolean             = false,
    paginated:              Boolean             = false
  )(implicit refFactory: ActorRefFactory) = {
    val fullParams = if (paginated) queryParams + ("per_page" → config.itemsPerPage.toString) else queryParams
    val mediaType = if (useTestMediaType) TestMediaType else PermissionMediaType
    val uri = req.uri.withQuery(fullParams ++ req.uri.query)
    HttpClient(req.copy(uri = uri))
      .withHeader(HttpHeaders.Accept.name, mediaType)
      .withHeader(HttpHeaders.Authorization.name, s"token ${token.getOrElse(config.apiToken)}")
  }

  protected def fetchAllPages[T: FRU](url: String, queryParams: Map[String, String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FRU[List[T]]) = {

    def findNextPageUrl(linkHeader: Option[String]): Option[String] =
      linkHeader.flatMap { links ⇒
        links.split(",").collectFirst {
          case PagesNavRegex(link, rel) if rel == "next" ⇒ link
        }
      }
    def fetchAux(url: String, alreadyFetched: Future[List[T]])(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[T]] = {
      val req: HttpRequest = Get(url)
      baseRequest(req, queryParams, useTestMediaType = true, paginated = true)
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

  protected def fetchMultiple[T](route: String, errorMsg: String, params: Map[String, String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FromResponseUnmarshaller[List[T]]): Future[List[T]] = {
    val url = s"${config.apiUrl}/$route"
    val req: HttpRequest = Get(url)
    baseRequest(req, params).executeRequestInto[List[T]]().map {
      case SuccessfulRequest(list, _) ⇒ list
      case FailedRequest(statusCode) ⇒
        logger.error(s"$errorMsg, status code: ${statusCode.intValue}")
        Nil
    }
  }

  protected def fetchMultiple[T](route: String, errorMsg: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FRU[List[T]]): Future[List[T]] =
    fetchMultiple(route, errorMsg, Map.empty)

  protected def fetchOptional[T](route: String, errorMsg: String, params: Map[String, String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FRU[Option[T]]): Future[Option[T]] = {
    val url = s"${config.apiUrl}/$route"
    val req: HttpRequest = Get(url)
    baseRequest(req, params).executeRequestInto[Option[T]]().map {
      case SuccessfulRequest(o, _) ⇒ o
      case FailedRequest(statusCode) ⇒
        logger.error(s"$errorMsg, status code: ${statusCode.intValue}")
        None
    }
  }

  protected def fetchOptional[T](route: String, errorMsg: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FRU[Option[T]]): Future[Option[T]] =
    fetchOptional(route, errorMsg, Map.empty)

}
