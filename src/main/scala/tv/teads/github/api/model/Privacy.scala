package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait Privacy
object Privacy extends Enumerated[Privacy] {
  val values = List(secret, closed)

  case object secret extends Privacy
  case object closed extends Privacy
}