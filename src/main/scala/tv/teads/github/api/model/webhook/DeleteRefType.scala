package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait DeleteRefType
object DeleteRefType extends Enumerated[DeleteRefType] {
  val values = List(branch, tag)

  case object branch extends DeleteRefType
  case object tag extends DeleteRefType
}