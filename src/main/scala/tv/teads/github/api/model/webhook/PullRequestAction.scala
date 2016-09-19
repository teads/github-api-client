package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait PullRequestAction
object PullRequestAction extends Enumerated[PullRequestAction] {
  val values = List(assigned, unassigned, labeled, unlabeled, opened, closed, reopened, synchronize, edited)

  case object assigned extends PullRequestAction
  case object unassigned extends PullRequestAction
  case object labeled extends PullRequestAction
  case object unlabeled extends PullRequestAction
  case object opened extends PullRequestAction
  case object closed extends PullRequestAction
  case object reopened extends PullRequestAction
  case object synchronize extends PullRequestAction
  case object edited extends PullRequestAction
}