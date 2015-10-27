package tv.teads.github.api.json

import io.circe._
import io.circe.jawn._
import spray.http.MediaTypes._
import spray.http._
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.{Deserialized, MalformedContent, SimpleUnmarshaller, Unmarshaller}

/**
 * A trait providing automatic to and from JSON marshalling/unmarshalling using in-scope *play-json* Reads/Writes.
 * Note that *spray-httpx* does not have an automatic dependency on *play-json*.
 * You'll need to provide the appropriate *play-json* artifacts yourself.
 */
trait CirceSupport {

  implicit def circeUnmarshaller[T: Decoder] =
    delegate[String, T](`application/json`) { string ⇒
      Decoder[T].decodeJson(parse(string).getOrElse(Json.empty)).fold(
        failure ⇒ Left(MalformedContent(s"Received JSON is not valid.\n${failure.message}")),
        decoded ⇒ Right(decoded)
      )
    }(UTF8StringUnmarshaller)

  implicit def circeMarshaller[T: Encoder](implicit printer: Json ⇒ String = json ⇒ json.noSpaces) =
    Marshaller.delegate[T, String](ContentTypes.`application/json`) { value ⇒
      printer(Encoder[T].apply(value))
    }

  private val UTF8StringUnmarshaller = new Unmarshaller[String] {
    def apply(entity: HttpEntity) = Right(entity.asString(defaultCharset = HttpCharsets.`UTF-8`))
  }

  // Unmarshaller.delegate is used as a kind of map operation; play-json JsResult can contain either validation errors or the JsValue
  // representing a JSON object. We need a delegate method that works as a flatMap and let the provided A ⇒ Deserialized[B] function
  // to deal with any possible error, including exceptions.
  //
  private def delegate[A, B](unmarshalFrom: ContentTypeRange*)(f: A ⇒ Deserialized[B])(implicit ma: Unmarshaller[A]): Unmarshaller[B] =
    new SimpleUnmarshaller[B] {
      val canUnmarshalFrom = unmarshalFrom
      def unmarshal(entity: HttpEntity) = ma(entity).right.flatMap(a ⇒ f(a))
    }
}

object CirceSupport extends CirceSupport