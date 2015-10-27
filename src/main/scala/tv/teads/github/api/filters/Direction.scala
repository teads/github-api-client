package tv.teads.github.api.filters

import tv.teads.github.api.util.Enumerated

sealed trait Direction
object Direction extends Enumerated[Direction] {
  val values = List(asc, desc)

  case object asc extends Direction
  case object desc extends Direction
}
