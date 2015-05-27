package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait CommentFormats {
  self:UserFormats =>
  implicit lazy val  commentJsonWrite: Write[Comment, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Comment, JsObject]
  }

  implicit lazy val  commentrJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Comment]
  }
}


case class Comment(
                    url: String,
                    html_url: String,
                    issue_url: String,
                    id: Long,
                    user: User,
                    created_at: String,
                    updated_at: String,
                    body: String
                    )
