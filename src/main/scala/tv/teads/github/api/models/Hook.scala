package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.libs.json.JsValue
import play.api.data.mapping._

trait HookFormats {
  self: HookConfigFormats ⇒

  implicit lazy val hookJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "id").read[Long] ~
      (__ \ "url").read[String] ~
      (__ \ "ping_url").read[String] ~
      (__ \ "name").read[String] ~
      (__ \ "events").read[List[String]] ~
      (__ \ "active").read[Boolean] ~
      (__ \ "config").read[HookConfig] ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "created_at").read(zonedDateTime)
    )(Hook.apply _)
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