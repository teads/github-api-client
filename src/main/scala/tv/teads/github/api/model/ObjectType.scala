package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait ObjectType

object ObjectType extends Enumerated[ObjectType] {
  val values = List(commit, tag)

  case object commit extends ObjectType
  case object tag extends ObjectType
}
