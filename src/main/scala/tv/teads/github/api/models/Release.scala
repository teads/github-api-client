package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait ReleaseFormats {
  self: UserFormats with AuthorFormats ⇒
  implicit lazy val releaseJsonWrite: Write[Release, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[Release, JsObject]
  }

  implicit lazy val releaseJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "url").read[String] ~
      (__ \ "assets_url").read[String] ~
      (__ \ "upload_url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "tag_name").read[String] ~
      (__ \ "target_commitish").read[String] ~
      (__ \ "name").read[String] ~
      (__ \ "draft").read[Boolean] ~
      (__ \ "author").read[Author] ~
      (__ \ "prerelease").read[Boolean] ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "published_at").read(jodaLongOrISO) ~
      (__ \ "assets").read[List[String]] ~
      (__ \ "tarball_url").read[String] ~
      (__ \ "zipball_url").read[String] ~
      (__ \ "body").read[String]
    )(Release.apply _)
  }

}
case class Release(
  url:              String,
  assets_url:       String,
  upload_url:       String,
  html_url:         String,
  id:               Long,
  tag_name:         String,
  target_commitish: String,
  name:             String,
  draft:            Boolean,
  author:           Author,
  prerelease:       Boolean,
  created_at:       DateTime,
  published_at:     DateTime,
  assets:           List[String],
  tarball_url:      String,
  zipball_url:      String,
  body:             String
)
