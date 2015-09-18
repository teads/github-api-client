package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import spray.httpx.unmarshalling._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.util._
import tv.teads.github.api.services.GithubConfiguration.configuration

import scala.concurrent.{ Future, ExecutionContext }

trait GithubService extends Service with PayloadFormats {

  protected val DefaultMediaType = "application/vnd.github.v3+json"
  protected val TestMediaType = "application/vnd.github.moondragon+json"
  protected val RawContentMediaType = "application/vnd.github.v3.raw"
  protected val PagesNavRegex = """(?:\s*)<(.+)>; rel=(.+)""".r

  protected def baseRequest(
    req:              HttpRequest,
    queryParams:      Map[String, String],
    useTestMediaType: Boolean             = false,
    paginated:        Boolean             = false
  )(implicit refFactory: ActorRefFactory) = {
    val paramsWithToken = queryParams + configuration.api.tokenHeader
    val fullParams = if (paginated) paramsWithToken + configuration.api.paginationHeader else paramsWithToken
    val mediaType = if (useTestMediaType) TestMediaType else DefaultMediaType
    val uri = req.uri.withQuery(fullParams ++ req.uri.query)
    HttpClient(req.copy(uri = uri))
      .withHeader(HttpHeaders.Accept.name, mediaType)
  }

  protected def fetchAllPages[T: FromResponseUnmarshaller](url: String, queryParams: Map[String, String])(implicit refFactory: ActorRefFactory, ec: ExecutionContext, ev: FromResponseUnmarshaller[List[T]]) = {

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

}
