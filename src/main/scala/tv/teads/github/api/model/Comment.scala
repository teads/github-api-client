package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._

import tv.teads.github.api.json.ZonedDateTimeCodec

trait CommentCodec {
  self: UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val commentDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      issueUrl ← cursor.downField("issue_url").as[String]
      id ← cursor.downField("id").as[Long]
      user ← cursor.downField("user").as[User]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      body ← cursor.downField("body").as[String]
    } yield Comment(url, htmlUrl, issueUrl, id, user, createdAt, updatedAt, body)
  }
}

case class Comment(
  url:       String,
  htmlUrl:   String,
  issueUrl:  String,
  id:        Long,
  user:      User,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  body:      String
)
