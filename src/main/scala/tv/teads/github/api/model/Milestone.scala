package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait MilestoneCodec {
  self: UserCodec with LabelCodec with ZonedDateTimeCodec ⇒

  implicit lazy val milestoneDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      labelsUrl ← cursor.downField("labels_url").as[String]
      id ← cursor.downField("id").as[Long]
      number ← cursor.downField("number").as[Long]
      state ← cursor.downField("state").as[String]
      title ← cursor.downField("title").as[String]
      description ← cursor.downField("description").as[Option[String]]
      creator ← cursor.downField("creator").as[User]
      openIssues ← cursor.downField("open_issues").as[Long]
      closedIssues ← cursor.downField("closed_issues").as[Long]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      closedAt ← cursor.downField("closed_at").as[Option[ZonedDateTime]]
      dueOn ← cursor.downField("due_on").as[Option[ZonedDateTime]]
    } yield Milestone(url, htmlUrl, labelsUrl, id, number, state, title, description, creator, openIssues, closedIssues,
      createdAt, updatedAt, closedAt, dueOn)
  }
}

case class Milestone(
  url:          String,
  htmlUrl:      String,
  labelsUrl:    String,
  id:           Long,
  number:       Long,
  state:        String,
  title:        String,
  description:  Option[String],
  creator:      User,
  openIssues:   Long,
  closedIssues: Long,
  createdAt:    ZonedDateTime,
  updatedAt:    ZonedDateTime,
  closedAt:     Option[ZonedDateTime],
  dueOn:        Option[ZonedDateTime]
)
