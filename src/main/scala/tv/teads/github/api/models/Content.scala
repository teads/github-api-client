package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{ JsObject, JsValue }

trait ContentFormats {
  self: UserFormats with LinksContentFormats ⇒
  implicit lazy val contentJsonWrite: Write[Content, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Content, JsObject]
  }

  implicit lazy val contentJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Content]
  }
}
case class Content(
  `type`:       String,
  encoding:     String,
  size:         Double,
  name:         String,
  path:         String,
  content:      String,
  sha:          String,
  url:          String,
  git_url:      String,
  html_url:     String,
  download_url: String,
  _links:       LinksContent
)
