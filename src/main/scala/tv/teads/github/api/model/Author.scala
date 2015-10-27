package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait AuthorCodec {
  implicit lazy val authorEncoder = deriveFor[Author].encoder
  implicit lazy val authorDecoder = deriveFor[Author].decoder
}

case class Author(
  name:     String,
  email:    String,
  username: Option[String] = None,
  date:     Option[String] = None
)
