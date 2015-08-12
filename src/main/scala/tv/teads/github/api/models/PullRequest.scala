package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.data.mapping._
import play.api.libs.json.{ JsString, JsObject, JsValue }

trait PullRequestUrlsFormats {
  implicit lazy val pullRequestUrlsJsonWrite: Write[PullRequestUrls, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestUrls, JsObject]
  }

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
  html_url:            String,
  diff_url:            String,
  patch_url:           String,
  issue_url:           String,
  commits_url:         String,
  review_comments_url: String,
  review_comment_url:  String,
  comments_url:        String,
  statuses_url:        String
)

trait TimeMetadataFormats {
  implicit lazy val timeMetadataJsonWrite: Write[TimeMetadata, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._

    Write.gen[TimeMetadata, JsObject]
  }

  implicit lazy val timeMetadataJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._

    (
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "closed_at").read(optionR(jodaLongOrISO)) ~
      (__ \ "merged_at").read(optionR(jodaLongOrISO)) ~
      (__ \ "merge_commit_sha").read[Option[String]]
    )(TimeMetadata.apply _)
  }

}
case class TimeMetadata(
  created_at:       DateTime,
  updated_at:       DateTime,
  closed_at:        Option[DateTime],
  merged_at:        Option[DateTime],
  merge_commit_sha: Option[String]
)

trait ChangeMetadataFormats {
  implicit lazy val changeMetadataJsonWrite: Write[ChangeMetadata, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[ChangeMetadata, JsObject]
  }

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
  comments:        Option[Long],
  review_comments: Option[Long],
  commits:         Option[Long],
  additions:       Option[Long],
  deletions:       Option[Long],
  changed_files:   Option[Long]

)

trait PullRequestFormats {
  self: UserFormats with PullRequestUrlsFormats with LinksFormats with TimeMetadataFormats with ChangeMetadataFormats with HeadFormats ⇒
  implicit lazy val pullRequestJsonWrite: Write[PullRequest, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequest, JsObject]
  }

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
  url:             String,
  id:              Long,
  number:          Long,
  state:           String,
  locked:          Boolean,
  title:           String,
  user:            User,
  body:            String,
  assignee:        Option[User],
  milestone:       Option[String],
  head:            Head,
  base:            Head,
  merged:          Option[Boolean],
  mergeable:       Option[Boolean],
  mergeable_state: Option[String],
  merged_by:       Option[User],
  links:           Links,
  urls:            PullRequestUrls,
  timeMetadata:    TimeMetadata,
  changeMetadata:  ChangeMetadata
)
