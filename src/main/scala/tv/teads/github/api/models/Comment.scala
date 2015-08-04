package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.data.mapping._
import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.StatusStates.StatusState

trait CommentFormats {
  self: UserFormats ⇒
  implicit lazy val commentJsonWrite: Write[Comment, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[Comment, JsObject]
  }

  implicit lazy val commentrJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "issue_url").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "user").read[User] ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "body").read[String]
    )(Comment.apply _)
  }
}

case class Comment(
  url:        String,
  html_url:   String,
  issue_url:  String,
  id:         Long,
  user:       User,
  created_at: DateTime,
  updated_at: DateTime,
  body:       String
)
