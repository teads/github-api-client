package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait PullRequestCommentFormats {
  self: UserFormats with PullRequestReviewCommentLinksFormats  =>
  implicit lazy val pullRequestCommentJsonWrite: Write[PullRequestReviewComment, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestReviewComment, JsObject]
  }

  implicit lazy val pullRequestCommentJsonRead = {
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    Rule.gen[JsValue, PullRequestReviewComment]
  }

}

case class PullRequestReviewComment(
                               url: String,
                               id: Long,
                               diff_hunk: String,
                               path: String,
                               position: Option[Long],
                               original_position: Option[Long],
                               commit_id: String,
                               original_commit_id: String,
                               user: User,
                               body: String,
                               created_at: String,
                               updated_at: String,
                               html_url: String,
                               pull_request_url: String,
                               _links: PullRequestReviewCommentLinks
                               )
