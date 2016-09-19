package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait RepositoryAction
object RepositoryAction extends Enumerated[RepositoryAction] {
  val values = List(created, deleted, publicized, privatized)

  case object created extends RepositoryAction
  case object deleted extends RepositoryAction
  case object publicized extends RepositoryAction
  case object privatized extends RepositoryAction
}