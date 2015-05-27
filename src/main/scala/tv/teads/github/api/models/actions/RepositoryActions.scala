package tv.teads.github.api.models.actions

import tv.teads.github.api.models.common.ADTEnum

object RepositoryActions {

  sealed trait RepositoryAction
  object RepositoryAction extends ADTEnum[RepositoryAction] {

    case object created    extends RepositoryAction
    val list = Seq(created)
  }

}
