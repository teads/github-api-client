package tv.teads.github.api.services

import com.typesafe.scalalogging.StrictLogging
import io.circe.Decoder
import okhttp3.Request
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http.ResponseWrapper

import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractGithubService(config: GithubApiClientConfig) extends StrictLogging {
  protected val DefaultMediaType = "application/vnd.github.v3+json"

  protected def baseRequest(
    requestBuilder: Request.Builder,
    mediaType:      String
  ): Future[ResponseWrapper] = {
    val requestBuilderWithMediaType = requestBuilder.addHeader("Accept", mediaType)
    config.client.executeAsync(requestBuilderWithMediaType)
  }

  protected def baseGet(route: String, mediaType: String): Future[ResponseWrapper] = {
    val requestBuilder = new Request.Builder().url(s"${config.apiUrl}/$route").get()
    baseRequest(requestBuilder, mediaType)
  }

  protected def get[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[T] = {
    baseGet(route, mediaType).flatMap {
      _.as[T].fold(
        code ⇒ Future.failed(new RuntimeException(s"$errorMessage, status code: $code")),
        resp ⇒ Future.successful(resp.decoded)
      )
    }
  }

  protected def getRawOptional(
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[Option[String]] = baseGet(route, mediaType).map(_.rawOptional)

  protected def getOptional[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String = DefaultMediaType
  )(implicit ec: ExecutionContext): Future[Option[T]] = {
    baseGet(route, mediaType).map {
      _.as[Option[T]].fold(
        code ⇒ failedRequestWithDefaultValue(errorMessage, code, None),
        _.decoded
      )
    }
  }

  protected def failedRequestWithDefaultValue[T](errorMessage: String, statusCode: Int, defaultValue: T) = {
    logger.error(s"$errorMessage, status code: $statusCode")
    defaultValue
  }

}
