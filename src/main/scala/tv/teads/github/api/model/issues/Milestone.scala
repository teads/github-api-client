package tv.teads.github.api.model.issues

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.common.GithubAccount
import tv.teads.github.api.model.DateTimeCodecs.IsoOffsetToZoneDateTimeDecoder

object Milestone {

  implicit final val MilestoneDecoder =
    Decoder.forProduct14(
      "url", "html_url", "labels_url", "id", "state", "title",
      "description", "creator", "open_issues", "closed_issues",
      "created_at", "updated_at", "closed_at", "due_on"
    )(Milestone.apply)
}
case class Milestone(
  url:          String,
  htmlUrl:      String,
  labelsUrl:    String,
  id:           Int,
  state:        MilestoneState,
  title:        String,
  description:  String,
  creator:      GithubAccount,
  openIssues:   Int,
  closedIssues: Int,
  createdAt:    ZonedDateTime,
  updatedAt:    ZonedDateTime,
  closedAt:     Option[ZonedDateTime],
  dueOn:        Option[ZonedDateTime]
)