package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object PullRequestActions {

  sealed trait PullRequestAction
  // https://developer.github.com/v3/activity/events/types/#pullrequestevent
  object PullRequestAction extends ADTEnum[PullRequestAction] {

    case object assigned    extends PullRequestAction
    case object unassigned  extends PullRequestAction
    case object labeled     extends PullRequestAction
    case object unlabeled   extends PullRequestAction
    case object opened      extends PullRequestAction
    case object closed      extends PullRequestAction
    case object reopened    extends PullRequestAction
    case object synchronize extends PullRequestAction


    val list = Seq(
      assigned    ,
      unassigned  ,
      labeled     ,
      unlabeled   ,
      opened      ,
      closed      ,
      reopened    ,
      synchronize
    )
  }

}
