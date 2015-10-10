package tv.teads.github.api.util

import play.api.data.mapping.{Failure, Success, Rule, Write}
import play.api.data.mapping.json.Rules._
import play.api.data.mapping.json.Writes._
import play.api.libs.json.{Json, JsValue}
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.{Deserialized, MalformedContent, SimpleUnmarshaller, Unmarshaller}
import spray.http._
import MediaTypes._

/**
 * A trait providing automatic to and from JSON marshalling/unmarshalling using in-scope *play-json* Reads/Writes.
 * Note that *spray-httpx* does not have an automatic dependency on *play-json*.
 * You'll need to provide the appropriate *play-json* artifacts yourself.
 */
trait ValidationJsonSupport {
  type R[T] = Rule[JsValue, T]
  type W[T] = Write[T, JsValue]

  implicit def validationJsonUnmarshaller[T: R] =
    delegate[String, T](`application/json`)(string ⇒

      implicitly[R[T]].validate(Json.parse(string)) match {
        case Success(s) ⇒ Right(s)
        case Failure(e) ⇒ Left(MalformedContent(s"Received JSON is not valid.\n$e"))
      })(UTF8StringUnmarshaller)

  implicit def validationJsonMarshaller[T: W](implicit printer: JsValue ⇒ String = Json.stringify) =
    Marshaller.delegate[T, String](ContentTypes.`application/json`) { value ⇒
      printer(implicitly[W[T]].writes(value))
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

object ValidationJsonSupport extends ValidationJsonSupport