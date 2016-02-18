package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait AuthorCodec {
  implicit lazy val authorEncoder = deriveEncoder[Author]
  implicit lazy val authorDecoder = deriveDecoder[Author]
}

case class Author(
  name:     String,
  email:    String,
  username: Option[String] = None,
  date:     Option[String] = None
)
