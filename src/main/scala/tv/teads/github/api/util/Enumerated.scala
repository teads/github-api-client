package tv.teads.github.api.util

import scala.reflect.ClassTag

import cats.data.Xor
import io.circe._

abstract class Enumerated[T: ClassTag] {
  def values: List[T]

  lazy implicit val enumDecoder = Decoder.instance { cursor ⇒
    cursor.as[String].flatMap { s ⇒
      values
        .find(_.toString == s).map(Xor.right).
        getOrElse(Xor.left(DecodingFailure(implicitly[ClassTag[T]].runtimeClass.getName, cursor.history)))
    }
  }

  lazy implicit val enumEncoder = Encoder.instance[T](v ⇒ Json.string(v.toString))
}
