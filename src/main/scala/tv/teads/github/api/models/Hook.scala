package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.libs.json.{JsObject, JsValue}
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
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "created_at").read(jodaLongOrISO)
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
  updatedAt: DateTime,
  createdAt: DateTime
)