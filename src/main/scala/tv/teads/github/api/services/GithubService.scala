package tv.teads.github.api.services

import scala.concurrent.{Future, ExecutionContext}

import okhttp3._
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._

abstract class GithubService(config: GithubApiClientConfig) extends LazyLogging {

  protected def emptyRequestBody = RequestBody.create(null, new Array[Byte](0))

  private val PagesNavRegex = """(?:\s*)<(.+)>; rel="(.+)"""".r

  protected def baseRequest(
    requestBuilder: Request.Builder,
    mediaType:      String          = GithubMediaTypes.DefaultMediaType,
    customToken:    Option[String]  = None
  ): Future[Response] = {
    val requestBuilderWithMediaType = requestBuilder.addHeader("Accept", mediaType)
    val optionalAuth = customToken.map(Authenticators.apiTokenAuthenticator)
    config.client.executeAsync(requestBuilderWithMediaType, optionalAuth)
  }

  protected def fetchAllPages[T: Decoder](
    route:        String,
    errorMessage: String,
    params:       Map[String, String] = Map.empty,
    customToken:  Option[String]      = None
  )(implicit ec: ExecutionContext): Future[List[T]] = {
    def findNextPageUrl(linkHeader: Option[String]): Option[String] =
      linkHeader.flatMap(_.split(",").collectFirst { case PagesNavRegex(link, rel) if rel == "next" ⇒ link })

    def fetchAux(url: String, alreadyFetched: Future[List[T]])(implicit ec: ExecutionContext): Future[List[T]] = {
      val httpUrlBuilder = HttpUrl.parse(url).newBuilder()
      val builderWithParams = httpUrlBuilder.addAllParams(params + ("per_page" → config.itemsPerPage.toString))
      val requestBuilder = new Request.Builder().url(builderWithParams.build()).get()

      baseRequest(requestBuilder, GithubMediaTypes.TestMediaType, customToken).flatMap {
        _.as[List[T]].fold(
          code ⇒ failedRequest(errorMessage, code, alreadyFetched),
          decodedResponse ⇒ {
            val lastFetched = alreadyFetched.map(_ ++ decodedResponse.decoded)
            findNextPageUrl(Option(decodedResponse.rawResponse.headers().get("Link")))
              .map(nextUrl ⇒ fetchAux(nextUrl, lastFetched)).getOrElse(lastFetched)
          }
        )
      }
    }

    fetchAux(s"${config.apiUrl}/$route", Future.successful(Nil))
  }

  protected def fetchMultiple[T](
    route:        String,
    errorMessage: String,
    params:       Map[String, String] = Map.empty,
    mediaType:    String              = GithubMediaTypes.DefaultMediaType,
    customToken:  Option[String]      = None
  )(implicit ec: ExecutionContext, ev: Decoder[List[T]]): Future[List[T]] = {
    val httpUrl = HttpUrl.parse(s"${config.apiUrl}/$route").newBuilder().addAllParams(params).build()
    val requestBuilder = new Request.Builder().url(httpUrl).get()
    baseRequest(requestBuilder, customToken = customToken).map {
      _.as[List[T]].fold(
        code ⇒ failedRequest(errorMessage, code, Nil),
        _.decoded
      )
    }
  }

  protected def fetchOptional[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String         = GithubMediaTypes.DefaultMediaType,
    customToken:  Option[String] = None
  )(implicit ec: ExecutionContext): Future[Option[T]] = {
    val requestBuilder = new Request.Builder().url(s"${config.apiUrl}/$route").get()
    baseRequest(requestBuilder, customToken = customToken).map {
      _.as[Option[T]].fold(
        code ⇒ failedRequest(errorMessage, code, None),
        _.decoded
      )
    }
  }

  protected def failedRequest[T](errorMessage: String, statusCode: Int, defaultValue: T) = {
    logger.error(s"$errorMessage, status code: $statusCode")
    defaultValue
  }
}
