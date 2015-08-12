package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait IssueFormats {
  self: UserFormats with LabelFormats ⇒
  implicit lazy val issueJsonWrite: Write[Issue, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[Issue, JsObject]
  }

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
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "closed_at").read(optionR(jodaLongOrISO)) ~
      (__ \ "body").read[String]
    )(Issue.apply _)
  }

}
case class Issue(
  url:          String,
  labels_url:   String,
  comments_url: String,
  events_url:   String,
  html_url:     String,
  id:           Long,
  number:       Long,
  title:        String,
  user:         User,
  labels:       List[Label],
  state:        String,
  locked:       Boolean,
  assignee:     Option[User],
  milestone:    Option[String],
  comments:     Long,
  created_at:   DateTime,
  updated_at:   DateTime,
  closed_at:    Option[DateTime],
  body:         String
)
