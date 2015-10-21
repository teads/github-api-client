package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait IssueFormats {
  self: UserFormats with LabelFormats ⇒

  implicit lazy val issueJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "url").read[String] ~
      (__ \ "labels_url").read[String] ~
      (__ \ "comments_url").read[String] ~
      (__ \ "events_url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "number").read[Long] ~
      (__ \ "title").read[String] ~
      (__ \ "user").read[User] ~
      (__ \ "labels").read[List[Label]] ~
      (__ \ "state").read[String] ~
      (__ \ "locked").read[Boolean] ~
      (__ \ "assignee").read[Option[User]] ~
      (__ \ "milestone").read[Option[String]] ~
      (__ \ "comments").read[Long] ~
      (__ \ "created_at").read(zonedDateTime) ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "closed_at").read(optionR(zonedDateTime)) ~
      (__ \ "body").read[String]
    )(Issue.apply _)
  }

}
case class Issue(
  url:         String,
  labelsUrl:   String,
  commentsUrl: String,
  eventsUrl:   String,
  htmlUrl:     String,
  id:          Long,
  number:      Long,
  title:       String,
  user:        User,
  labels:      List[Label],
  state:       String,
  locked:      Boolean,
  assignee:    Option[User],
  milestone:   Option[String],
  comments:    Long,
  createdAt:   ZonedDateTime,
  updatedAt:   ZonedDateTime,
  closedAt:    Option[ZonedDateTime],
  body:        String
)
