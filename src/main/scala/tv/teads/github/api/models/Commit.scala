package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait CommitFormats {
  self: UserFormats with AuthorFormats ⇒
  implicit lazy val commitJsonWrite: Write[Commit, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Commit, JsObject]
  }

  implicit lazy val commitJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Commit]
  }
}

case class Commit(
  id:        String,
  distinct:  Boolean,
  message:   String,
  timestamp: String,
  url:       String,
  author:    Author,
  committer: Author,
  added:     List[String],
  removed:   List[String],
  modified:  List[String]
)
