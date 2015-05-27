package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object IssueActions {

  sealed trait IssueAction
  // https://developer.github.com/v3/activity/events/types/#pullrequestevent
  object IssueAction extends ADTEnum[IssueAction] {

    case object assigned    extends IssueAction
    case object unassigned  extends IssueAction
    case object labeled     extends IssueAction
    case object unlabeled   extends IssueAction
    case object opened      extends IssueAction
    case object closed      extends IssueAction
    case object reopened    extends IssueAction


    val list = Seq(
      assigned    ,
      unassigned  ,
      labeled     ,
      unlabeled   ,
      opened      ,
      closed      ,
      reopened
    )
  }

}
