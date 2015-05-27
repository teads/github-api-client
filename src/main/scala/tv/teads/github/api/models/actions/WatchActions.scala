package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object WatchActions {

  sealed trait WatchAction
  // https://developer.github.com/v3/activity/events/types/#pullrequestevent
  object WatchAction extends ADTEnum[WatchAction] {

    case object started    extends WatchAction


    val list = Seq(
      started
    )
  }

}
