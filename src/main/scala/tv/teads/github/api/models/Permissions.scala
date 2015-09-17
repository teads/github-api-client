package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object Permissions {

  sealed trait Permission

  object Permission extends ADTEnum[Permission] {

    case object pull extends Permission
    case object push extends Permission
    case object admin extends Permission

    val list = Seq(
      pull, push, admin
    )
  }

}
