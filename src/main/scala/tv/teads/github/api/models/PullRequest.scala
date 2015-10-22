package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.data.mapping._
import play.api.libs.json.{JsString, JsObject, JsValue}
import tv.teads.github.api.models.PullRequestStatuses.PullRequestStatus

trait PullRequestUrlsFormats {
  implicit lazy val pullRequestUrlsJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "html_url").read[String] ~
      (__ \ "diff_url").read[String] ~
      (__ \ "patch_url").read[String] ~
      (__ \ "issue_url").read[String] ~
      (__ \ "commits_url").read[String] ~
      (__ \ "review_comments_url").read[String] ~
      (__ \ "review_comment_url").read[String] ~
      (__ \ "comments_url").read[String] ~
      (__ \ "statuses_url").read[String]
    )(PullRequestUrls.apply _)
  }

}
case class PullRequestUrls(
  htmlUrl:           String,
  diffUrl:           String,
  patchUrl:          String,
  issueUrl:          String,
  commitsUrl:        String,
  reviewCommentsUrl: String,
  reviewCommentUrl:  String,
  commentsUrl:       String,
  statusesUrl:       String
)

trait TimeMetadataFormats {
  implicit lazy val timeMetadataJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._

    (
      (__ \ "created_at").read(zonedDateTime) ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "closed_at").read(optionR(zonedDateTime)) ~
      (__ \ "merged_at").read(optionR(zonedDateTime)) ~
      (__ \ "merge_commit_sha").read[Option[String]]
    )(TimeMetadata.apply _)
  }

}
case class TimeMetadata(
  createdAt:      ZonedDateTime,
  updatedAt:      ZonedDateTime,
  closedAt:       Option[ZonedDateTime],
  mergedAt:       Option[ZonedDateTime],
  mergeCommitSha: Option[String]
)

trait ChangeMetadataFormats {
  implicit lazy val changeMetadataJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "comments").read[Option[Long]] ~
      (__ \ "review_comments").read[Option[Long]] ~
      (__ \ "commits").read[Option[Long]] ~
      (__ \ "additions").read[Option[Long]] ~
      (__ \ "review_comments").read[Option[Long]] ~
      (__ \ "deletions").read[Option[Long]]
    )(ChangeMetadata.apply _)
  }

}
case class ChangeMetadata(
  comments:       Option[Long],
  reviewComments: Option[Long],
  commits:        Option[Long],
  additions:      Option[Long],
  deletions:      Option[Long],
  changedFiles:   Option[Long]

)

trait PullRequestFormats {
  self: UserFormats with PullRequestUrlsFormats with LinksFormats with TimeMetadataFormats with ChangeMetadataFormats with HeadFormats ⇒

  implicit lazy val pullRequestJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "url").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "number").read[Long] ~
      (__ \ "state").read[String] ~
      (__ \ "locked").read[Boolean] ~
      (__ \ "title").read[String] ~
      (__ \ "user").read[User] ~
      (__ \ "body").read[String] ~
      (__ \ "assignee").read[Option[User]] ~
      (__ \ "milestone").read[Option[String]] ~
      (__ \ "head").read[Head] ~
      (__ \ "base").read[Head] ~
      (__ \ "merged").read[Option[Boolean]] ~
      (__ \ "mergeable").read[Option[Boolean]] ~
      (__ \ "mergeable_state").read[Option[String]] ~
      (__ \ "merged_by").read[Option[User]] ~
      (__ \ "_links").read[Links] ~
      pullRequestUrlsJsonRead ~
      timeMetadataJsonRead ~
      changeMetadataJsonRead
    )(PullRequest.apply _)
  }

}
case class PullRequest(
    url:            String,
    id:             Long,
    number:         Long,
    state:          String,
    locked:         Boolean,
    title:          String,
    user:           User,
    body:           String,
    assignee:       Option[User],
    milestone:      Option[String],
    head:           Head,
    base:           Head,
    merged:         Option[Boolean],
    mergeable:      Option[Boolean],
    mergeableState: Option[String],
    mergedBy:       Option[User],
    links:          Links,
    urls:           PullRequestUrls,
    timeMetadata:   TimeMetadata,
    changeMetadata: ChangeMetadata
) {
  def status: PullRequestStatus =
    if (state == "open") PullRequestStatus.open
    else if (state == "closed" && timeMetadata.mergedAt.isDefined) PullRequestStatus.merged
    else PullRequestStatus.closed
}
