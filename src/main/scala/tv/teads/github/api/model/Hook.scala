package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait HookCodec {
  self: HookConfigCodec with ZonedDateTimeCodec ⇒

  implicit lazy val hookDecoder = Decoder.instance { cursor ⇒
    for {
      id ← cursor.downField("id").as[Long]
      url ← cursor.downField("url").as[String]
      pingUrl ← cursor.downField("ping_url").as[String]
      name ← cursor.downField("name").as[String]
      events ← cursor.downField("events").as[List[String]]
      active ← cursor.downField("active").as[Boolean]
      config ← cursor.downField("config").as[HookConfig]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
    } yield Hook(id, url, pingUrl, name, events, active, config, updatedAt, createdAt)
  }
}

case class Hook(
  id:        Long,
  url:       String,
  pingUrl:   String,
  name:      String,
  events:    List[String],
  active:    Boolean,
  config:    HookConfig,
  updatedAt: ZonedDateTime,
  createdAt: ZonedDateTime
)
