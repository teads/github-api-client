package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait WatchAction
object WatchAction extends Enumerated[WatchAction] {
  val values = List(started)

  case object started extends WatchAction
}