package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait IssueCommentAction
object IssueCommentAction extends Enumerated[IssueCommentAction] {
  val values = List(created)

  case object created extends IssueCommentAction
}