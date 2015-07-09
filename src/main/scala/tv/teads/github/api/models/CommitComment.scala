package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{ JsObject, JsValue }

trait CommitCommentFormats {
  self: UserFormats ⇒
  implicit lazy val commitCommentJsonWrite: Write[CommitComment, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[CommitComment, JsObject]
  }

  implicit lazy val commitCommentJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, CommitComment]
  }
}
case class CommitComment(
  url:        String,
  html_url:   String,
  id:         Long,
  user:       User,
  position:   Option[Long],
  line:       Option[Long],
  path:       Option[String],
  commit_id:  String,
  created_at: String,
  updated_at: String,
  body:       String
)
