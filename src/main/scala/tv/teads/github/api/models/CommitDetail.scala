package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{ JsObject, JsValue }

trait CommitDetailFormats {
  self: UserFormats with AuthorFormats with TreeFormats ⇒
  implicit lazy val commitDetailJsonWrite: Write[CommitDetail, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[CommitDetail, JsObject]
  }

  implicit lazy val commitDetailJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, CommitDetail]
  }
}
case class CommitDetail(
  author:        Author,
  committer:     Author,
  message:       String,
  tree:          Tree,
  url:           String,
  comment_count: Long
)
