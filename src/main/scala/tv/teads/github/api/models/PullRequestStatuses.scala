package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object PullRequestStatuses {

  sealed trait PullRequestStatus

  object PullRequestStatus extends ADTEnum[PullRequestStatus] {

    case object open extends PullRequestStatus
    case object closed extends PullRequestStatus
    case object merged extends PullRequestStatus

    val list = Seq(
      open, closed, merged
    )
  }

}
