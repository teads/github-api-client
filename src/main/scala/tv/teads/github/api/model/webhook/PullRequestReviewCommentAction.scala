package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait PullRequestReviewCommentAction
object PullRequestReviewCommentAction extends Enumerated[PullRequestReviewCommentAction] {
  val values = List(created)

  case object created extends PullRequestReviewCommentAction
}