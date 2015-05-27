package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object PullRequestCommentActions {

  sealed trait PullRequestCommentAction
  object PullRequestCommentAction extends ADTEnum[PullRequestCommentAction] {

    case object created    extends PullRequestCommentAction
    val list = Seq(created)
  }

}
