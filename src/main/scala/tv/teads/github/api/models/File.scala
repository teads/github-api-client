package tv.teads.github.api.models

import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait FileFormats {
  implicit lazy val fileJsonWrite: Write[File, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[File, JsObject]
  }

  implicit lazy val fileJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "sha").read[String] ~
      (__ \ "filename").read[String] ~
      (__ \ "status").read[String] ~
      (__ \ "additions").read[Long] ~
      (__ \ "deletions").read[Long] ~
      (__ \ "changes").read[Long] ~
      (__ \ "blob_url").read[String] ~
      (__ \ "raw_url").read[String] ~
      (__ \ "contents_url").read[String] ~
      (__ \ "patch").read[String]
    )(File.apply _)
  }

}

case class File(
  sha:          String,
  filename:     String,
  status:       String,
  additions:    Long,
  deletions:    Long,
  changes:      Long,
  blobUrl:     String,
  rawUrl:      String,
  contentsUrl: String,
  patch:        String
)
