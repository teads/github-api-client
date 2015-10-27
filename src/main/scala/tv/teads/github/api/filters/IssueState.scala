package tv.teads.github.api.filters

import tv.teads.github.api.util.Enumerated

sealed trait IssueState
object IssueState extends Enumerated[IssueState] {
  val values = List(open, closed, all)

  case object open extends IssueState
  case object closed extends IssueState
  case object all extends IssueState
}
