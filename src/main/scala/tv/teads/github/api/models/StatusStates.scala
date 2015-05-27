package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object StatusStates {

  sealed trait StatusState
  object StatusState extends ADTEnum[StatusState] {

    case object success    extends StatusState
    case object pending    extends StatusState
    case object failure    extends StatusState
    case object error      extends StatusState

    val list = Seq(
      success,
      pending,
      failure,
      error
    )
  }

}
