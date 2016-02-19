package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait IssueCodec {
  self: MilestoneCodec with UserCodec with LabelCodec with ZonedDateTimeCodec ⇒

  implicit lazy val issueDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      labelsUrl ← cursor.downField("labels_url").as[String]
      commentsUrl ← cursor.downField("comments_url").as[String]
      eventsUrl ← cursor.downField("events_url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      id ← cursor.downField("id").as[Long]
      number ← cursor.downField("number").as[Long]
      title ← cursor.downField("title").as[String]
      user ← cursor.downField("user").as[User]
      labels ← cursor.downField("labels").as[List[Label]]
      state ← cursor.downField("state").as[String]
      locked ← cursor.downField("locked").as[Boolean]
      assignee ← cursor.downField("assignee").as[Option[User]]
      milestone ← cursor.downField("milestone").as[Option[Milestone]]
      comments ← cursor.downField("comments").as[Long]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      closedAt ← cursor.downField("closed_at").as[Option[ZonedDateTime]]
      body ← cursor.downField("body").as[String]
    } yield Issue(
      url, labelsUrl, commentsUrl, eventsUrl, htmlUrl, id, number, title, user,
      labels, state, locked, assignee, milestone, comments, createdAt, updatedAt, closedAt, body
    )
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
  milestone:   Option[Milestone],
  comments:    Long,
  createdAt:   ZonedDateTime,
  updatedAt:   ZonedDateTime,
  closedAt:    Option[ZonedDateTime],
  body:        String
)
