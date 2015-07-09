package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object ReleaseActions {

  sealed trait ReleaseAction
  // https://developer.github.com/v3/activity/events/types/#pullrequestevent
  object ReleaseAction extends ADTEnum[ReleaseAction] {

    case object published extends ReleaseAction

    val list = Seq(
      published
    )
  }

}
