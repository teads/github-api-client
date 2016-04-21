package tv.teads.github.api.http

import okhttp3._
import com.typesafe.scalalogging.LazyLogging
import io.circe._

private[api] object Implicits extends LazyLogging {
  private val JsonPrinter = Printer(preserveOrder = true, dropNullKeys = true, indent = "")

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
