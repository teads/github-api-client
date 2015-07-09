package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object IssueCommentActions {

  sealed trait IssueCommentAction
  object IssueCommentAction extends ADTEnum[IssueCommentAction] {

    case object created extends IssueCommentAction
    val list = Seq(created)
  }

}
