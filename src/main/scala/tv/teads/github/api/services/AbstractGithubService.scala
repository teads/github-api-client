package tv.teads.github.api.services

import com.typesafe.scalalogging.StrictLogging
import io.circe.Decoder
import okhttp3.{Request, RequestBody}
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http.ResponseWrapper

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractGithubService(config: GithubApiClientConfig) extends StrictLogging {
  protected val DefaultMediaType = "application/vnd.github.v3+json"

  private def emptyRequestBody = RequestBody.create(null, new Array[Byte](0))

  private def baseRequestBuilder(route: String): Request.Builder =
    new Request.Builder().url(s"${config.apiUrl}/$route")

  private def baseRequest(requestBuilder: Request.Builder, mediaType: String): Future[ResponseWrapper] =
    config.client.executeAsync(requestBuilder.addHeader("Accept", mediaType))

  protected def baseGet(route: String, mediaType: String): Future[ResponseWrapper] =
    baseRequest(baseRequestBuilder(route).get(), mediaType)

  protected def get[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[T] =
    baseGet(route, mediaType).flatMap {
      _.as[T].fold(
        code ⇒ Future.failed(new RuntimeException(s"$errorMessage, status code: $code")),
        resp ⇒ Future.successful(resp.decoded)
      )
    }

  protected def getRawOptional(
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[Option[String]] =
    baseGet(route, mediaType).map(_.rawOptional)

  protected def getOptional[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[Option[T]] =
    baseGet(route, mediaType).map {
      _.as[Option[T]].fold(
        code ⇒ failedRequestWithDefaultValue(errorMessage, code, None),
        _.decoded
      )
    }

  protected def failedRequestWithDefaultValue[T](errorMessage: String, statusCode: Int, defaultValue: T) = {
    logger.error(s"$errorMessage, status code: $statusCode")
    defaultValue
  }

}
