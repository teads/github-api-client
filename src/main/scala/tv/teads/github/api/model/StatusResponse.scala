package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._

import tv.teads.github.api.json.ZonedDateTimeCodec

trait StatusResponseCodec {
  self: StatusCodec with UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val statusResponseDecoder = Decoder.instance { cursor ⇒
    for {
      status ← cursor.as[Status]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      id ← cursor.downField("id").as[Long]
      url ← cursor.downField("url").as[String]
      creator ← cursor.downField("creator").as[User]
    } yield StatusResponse(status, createdAt, updatedAt, id, url, creator)
  }
}

case class StatusResponse(
  status:    Status,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  id:        Long,
  url:       String,
  creator:   User
)
