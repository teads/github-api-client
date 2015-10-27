package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait ReleaseAction
object ReleaseAction extends Enumerated[ReleaseAction] {
  val values = List(published)

  case object published extends ReleaseAction
}