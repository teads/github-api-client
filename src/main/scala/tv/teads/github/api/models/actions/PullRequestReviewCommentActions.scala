package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object PullRequestReviewCommentActions {

  sealed trait PullRequestReviewCommentAction
  object PullRequestReviewCommentAction extends ADTEnum[PullRequestReviewCommentAction] {

    case object created    extends PullRequestReviewCommentAction
    val list = Seq(created)
  }

}
