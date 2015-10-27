package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait PullRequestStatus
object PullRequestStatus extends Enumerated[PullRequestStatus] {
  val values = List(open, closed, merged)

  case object open extends PullRequestStatus
  case object closed extends PullRequestStatus
  case object merged extends PullRequestStatus
}