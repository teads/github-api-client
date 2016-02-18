package tv.teads.github.api

import cats.data.Xor
import okhttp3._
import com.typesafe.scalalogging.LazyLogging
import io.circe._, io.circe.jawn._
import tv.teads.github.api.util.IO.withCloseable

package object http extends LazyLogging {
  private val JsonPrinter = Printer(preserveOrder = true, dropNullKeys = true, "")

  implicit class RichResponse(val underlying: Response) extends AnyVal {
    def as[T: Decoder]: Xor[Int, DecodedResponse[T]] = {
      val bodyString = withCloseable(underlying.body())(_.string())
      val json = parse(bodyString)

      json.flatMap(Decoder[T].decodeJson).bimap(
        error ⇒ logFailedDecoding(error, underlying.request().url().toString(), bodyString, underlying.code()),
        decoded ⇒ DecodedResponse(decoded, underlying)
      )
    }

    private def logFailedDecoding(error: Error, uri: String, body: String, statusCode: Int) = {
      logger.error(s"Failed to unmarshal response body from $uri: ${error.getMessage}")
      logger.debug(s"Response body was: $body")
      underlying.code()
    }
  }

  implicit class TypeToResponseBody[T](val value: T) extends AnyVal {
    def toJson(implicit encoder: Encoder[T]) =
      RequestBody.create(MediaType.parse("application/json"), encoder.apply(value).pretty(JsonPrinter))
  }

  implicit class RichHttpUrlBuilder(val httpUrlBuilder: HttpUrl.Builder) extends AnyVal {
    def addAllParams(params: Map[String, String]) =
      params.foldLeft(httpUrlBuilder) {
        case (builder, (key, value)) ⇒ builder.addEncodedQueryParameter(key, value)
      }
  }
}
