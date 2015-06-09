package tv.teads.github.api.filters.common

import tv.teads.github.api.models.common.ADTEnum

object States {

  sealed trait State
  object State extends ADTEnum[State] {

    case object open   extends State
    case object closed extends State
    case object all    extends State


    val list = Seq(
      open, closed, all
    )
  }

}
