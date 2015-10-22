package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait CommentFormats {
  self: UserFormats ⇒

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
      (__ \ "created_at").read(zonedDateTime) ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "body").read[String]
    )(Comment.apply _)
  }
}

case class Comment(
  url:       String,
  htmlUrl:   String,
  issueUrl:  String,
  id:        Long,
  user:      User,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  body:      String
)
