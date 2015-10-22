package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait ContentFormats {
  self: UserFormats with LinksContentFormats ⇒

  implicit lazy val contentJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "type").read[String] ~
      (__ \ "encoding").read[String] ~
      (__ \ "size").read[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "path").read[String] ~
      (__ \ "contents").read[String] ~
      (__ \ "sha").read[String] ~
      (__ \ "url").read[String] ~
      (__ \ "git_url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "download_url").read[String] ~
      (__ \ "_links").read[LinksContent]
    )(Content.apply _)
  }
}
case class Content(
  typ:         String,
  encoding:    String,
  size:        Long,
  name:        String,
  path:        String,
  content:     String,
  sha:         String,
  url:         String,
  gitUrl:      String,
  htmlUrl:     String,
  downloadUrl: String,
  links:       LinksContent
)
