package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait RepositoryAction
object RepositoryAction extends Enumerated[RepositoryAction] {
  val values = List(created)

  case object created extends RepositoryAction
}