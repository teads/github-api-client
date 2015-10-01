package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.actions.PullRequestReviewCommentActions
import tv.teads.github.api.models.actions.PullRequestReviewCommentActions.{ PullRequestReviewCommentAction, PullRequestReviewCommentAction$ }
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PullRequestCommentReviewPayloadFormats {
  self: UserFormats with RepositoryFormats with PullRequestFormats with PullRequestCommentFormats ⇒

  implicit lazy val pullRequestCommentReviewPayloadJsonWrite: Write[PullRequestCommentReviewPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestCommentReviewPayload, JsObject]
  }

  implicit lazy val pullRequestCommentReviewPayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "action").read[PullRequestReviewCommentAction] ~
      (__ \ "comment").read[PullRequestReviewComment] ~
      (__ \ "pull_request").read[PullRequest] ~
      (__ \ "repository").read[Repository] ~
      (__ \ "organization").read[Option[User]] ~
      (__ \ "sender").read[User]
    )(PullRequestCommentReviewPayload.apply _)
  }

}

case class PullRequestCommentReviewPayload(
  action:       PullRequestReviewCommentAction,
  comment:      PullRequestReviewComment,
  pull_request: PullRequest,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
