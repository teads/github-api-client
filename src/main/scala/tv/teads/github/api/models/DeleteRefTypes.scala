package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object DeleteRefTypes {

  sealed trait DeleteRefType
  object DeleteRefType extends ADTEnum[DeleteRefType] {

    case object branch        extends DeleteRefType
    case object tag           extends DeleteRefType

    val list = Seq(
      branch,
      tag
    )
  }

}
