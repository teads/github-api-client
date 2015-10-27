package tv.teads.github.api.model.webhook

import tv.teads.github.api.util.Enumerated

sealed trait CreateRefType
object CreateRefType extends Enumerated[CreateRefType] {
  val values = List(repository, branch, tag)

  case object repository extends CreateRefType
  case object branch extends CreateRefType
  case object tag extends CreateRefType
}