package tv.teads.github.api.http

import cats.data._, cats.std.list._
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.jawn._
import okhttp3.Response
import tv.teads.github.api.util.IO.withCloseable

class ResponseWrapper(val response: Response) extends LazyLogging {

  def rawOptional: Option[String] =
    if (response.isSuccessful) Some(withCloseable(response.body())(_.string()))
    else None

  def as[T: Decoder]: Validated[Int, DecodedResponse[T]] = {
    if (response.isSuccessful) {
      val bodyString = withCloseable(response.body())(_.string())
      val json = parse(bodyString).toValidated.toValidatedNel

      json.map(_.hcursor.acursor).andThen(Decoder[T].tryDecodeAccumulating).bimap(
        errors ⇒ logFailedDecoding(errors, response.request().url().toString, bodyString, response.code()),
        decoded ⇒ DecodedResponse(decoded, response)
      )
    } else {
      response.body().close()
      Validated.Invalid(response.code())
    }
  }

  private def logFailedDecoding[E <: Error](errors: NonEmptyList[E], uri: String, body: String, statusCode: Int): Int = {
    val formattedErrors = errors.unwrap.map(Error.showError.show)
    logger.error(s"Failed to unmarshal response body from $uri: ${formattedErrors.mkString(",")}")

    logger.error(s"Response body was: $body")
    response.code()
  }
}
