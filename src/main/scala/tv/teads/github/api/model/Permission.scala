package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait Permission
object Permission extends Enumerated[Permission] {
  val values = List(pull, push, admin)

  case object pull extends Permission
  case object push extends Permission
  case object admin extends Permission
}