package tv.teads.github.api.model

import io.circe._

trait ParentCodec {
  implicit lazy val parentDecoder = Decoder.instance { cursor ⇒
    for {
      sha ← cursor.downField("sha").as[String]
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
    } yield Parent(sha, url, htmlUrl)
  }
}
case class Parent(
  sha:     String,
  url:     String,
  htmlUrl: String
)
