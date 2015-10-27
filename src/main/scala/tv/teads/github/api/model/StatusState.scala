package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait StatusState
object StatusState extends Enumerated[StatusState] {
  val values = List(success, pending, failure, error)

  case object success extends StatusState
  case object pending extends StatusState
  case object failure extends StatusState
  case object error extends StatusState
}