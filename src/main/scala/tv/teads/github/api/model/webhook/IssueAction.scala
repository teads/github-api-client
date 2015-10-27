package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait IssueAction
object IssueAction extends Enumerated[IssueAction] {
  val values = List(assigned, unassigned, labeled, unlabeled, opened, closed, reopened)

  case object assigned extends IssueAction
  case object unassigned extends IssueAction
  case object labeled extends IssueAction
  case object unlabeled extends IssueAction
  case object opened extends IssueAction
  case object closed extends IssueAction
  case object reopened extends IssueAction
}