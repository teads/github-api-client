package tv.teads.github.api.http

import cats.data._, cats.std.list._
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.jawn._
import okhttp3.Response
import tv.teads.github.api.util.IO.withCloseable

private[api] class ResponseWrapper(val response: Response) extends LazyLogging {
  private val bodyString = withCloseable(response.body())(_.string())

  def isSuccessful = response.isSuccessful
  def raw: String = bodyString
  def rawIfExists: Validated[Int, String] =
    if (response.isSuccessful) Validated.Valid(raw)
    else Validated.invalid(response.code())

  def as[T: Decoder]: Validated[Int, DecodedResponse[T]] = {
    rawIfExists.andThen { raw ⇒
      val json = parse(raw).toValidated.toValidatedNel

      json.map(_.hcursor.acursor).andThen(Decoder[T].tryDecodeAccumulating).bimap(
        errors ⇒ logFailedDecoding(errors, response.request().url().toString, raw, response.code()),
        decoded ⇒ DecodedResponse(decoded, response)
      )
    }
  }

  private def logFailedDecoding[E <: Error](errors: NonEmptyList[E], uri: String, body: String, statusCode: Int): Int = {
    val formattedErrors = errors.unwrap.map(Error.showError.show)
    logger.error(s"Failed to unmarshal response body from $uri: ${formattedErrors.mkString(",")}")

    logger.error(s"Response body was: $body")
    response.code()
  }
}
