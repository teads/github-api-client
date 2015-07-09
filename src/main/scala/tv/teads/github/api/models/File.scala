package tv.teads.github.api.models

import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait FileFormats {
  implicit lazy val fileJsonWrite: Write[File, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[File, JsObject]
  }

  implicit lazy val fileJsonRead = {
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    Rule.gen[JsValue, File]
  }

}

case class File(
  sha:          String,
  filename:     String,
  status:       String,
  additions:    Long,
  deletions:    Long,
  changes:      Long,
  blob_url:     String,
  raw_url:      String,
  contents_url: String,
  patch:        String
)
