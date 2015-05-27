package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait TagFormats {
  self :UserFormats with TagCommitFormats =>
  implicit lazy val  tagJsonWrite : Write[Tag, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Tag, JsObject]
  }

  implicit lazy val  tagJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Tag]
  }

}
case class Tag(
                  name: String,
                  commit: TagCommit,
                  zipball_url: Option[String],
                  tarball_url: Option[String]
                  )
