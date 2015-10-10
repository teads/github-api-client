package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait PullRequestCommentFormats {
  self: UserFormats with PullRequestReviewCommentLinksFormats ⇒
  implicit lazy val pullRequestCommentJsonWrite: Write[PullRequestReviewComment, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[PullRequestReviewComment, JsObject]
  }

  implicit lazy val pullRequestCommentJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "url").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "diff_hunk").read[String] ~
      (__ \ "path").read[String] ~
      (__ \ "position").read[Option[Long]] ~
      (__ \ "original_position").read[Option[Long]] ~
      (__ \ "commit_id").read[String] ~
      (__ \ "original_commit_id").read[String] ~
      (__ \ "user").read[User] ~
      (__ \ "body").read[String] ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "html_url").read[String] ~
      (__ \ "pull_request_url").read[String] ~
      (__ \ "_links").read[PullRequestReviewCommentLinks]
    )(PullRequestReviewComment.apply _)
  }

}

case class PullRequestReviewComment(
  url:              String,
  id:               Long,
  diffHunk:         String,
  path:             String,
  position:         Option[Long],
  originalPosition: Option[Long],
  commitId:         String,
  originalCommitId: String,
  user:             User,
  body:             String,
  createdAt:        DateTime,
  updatedAt:        DateTime,
  htmlUrl:          String,
  pullRequestUrl:   String,
  links:            PullRequestReviewCommentLinks
)
