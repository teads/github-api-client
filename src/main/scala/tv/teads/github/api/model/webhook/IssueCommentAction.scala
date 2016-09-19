package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait IssueCommentAction
object IssueCommentAction extends Enumerated[IssueCommentAction] {
  val values = List(created, edited, deleted)

  case object created extends IssueCommentAction
  case object edited extends IssueCommentAction
  case object deleted extends IssueCommentAction
}