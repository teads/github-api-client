package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object CreateRefTypes {

  sealed trait CreateRefType
  object CreateRefType extends ADTEnum[CreateRefType] {

    case object repository extends CreateRefType
    case object branch extends CreateRefType
    case object tag extends CreateRefType

    val list = Seq(
      repository,
      branch,
      tag
    )
  }

}
