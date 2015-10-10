package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait TagFormats {
  self: UserFormats with TagCommitFormats ⇒
  implicit lazy val tagJsonWrite: Write[Tag, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Tag, JsObject]
  }

  implicit lazy val tagJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "name").read[String] ~
      (__ \ "commit").read[TagCommit] ~
      (__ \ "zipball_url").read[Option[String]] ~
      (__ \ "tarball_url").read[Option[String]]
    )(Tag.apply _)
  }

}
case class Tag(
  name:       String,
  commit:     TagCommit,
  zipballUrl: Option[String],
  tarballUrl: Option[String]
)
