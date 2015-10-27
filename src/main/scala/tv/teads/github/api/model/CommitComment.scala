package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait CommitCommentCodec {
  self: UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val commitCommentDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      id ← cursor.downField("id").as[Long]
      user ← cursor.downField("user").as[User]
      position ← cursor.downField("position").as[Option[Long]]
      line ← cursor.downField("line").as[Option[Long]]
      path ← cursor.downField("path").as[Option[String]]
      commitId ← cursor.downField("commit_id").as[String]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      body ← cursor.downField("body").as[String]
    } yield CommitComment(
      url, htmlUrl, id, user, position, line, path, commitId, createdAt, updatedAt, body
    )
  }
}
case class CommitComment(
  url:       String,
  htmlUrl:   String,
  id:        Long,
  user:      User,
  position:  Option[Long],
  line:      Option[Long],
  path:      Option[String],
  commitId:  String,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  body:      String
)
