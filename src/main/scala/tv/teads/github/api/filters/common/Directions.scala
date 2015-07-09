package tv.teads.github.api.filters.common

import tv.teads.github.api.models.common.ADTEnum

object Directions {

  sealed trait Direction
  object Direction extends ADTEnum[Direction] {

    case object asc extends Direction
    case object desc extends Direction

    val list = Seq(
      asc, desc
    )
  }

}
