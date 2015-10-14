package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait HookConfigFormats {
  implicit lazy val hookConfigJsonWrite: Write[HookConfig, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[HookConfig, JsObject]
  }

  implicit lazy val hookConfigJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "url").read[Option[String]] ~
      (__ \ "content_type").read[Option[String]]
    )(HookConfig.apply _)
  }

}
case class HookConfig(
  url:         Option[String],
  contentType: Option[String]
)