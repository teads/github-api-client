package tv.teads.github.api.model.issues

import enumeratum.EnumEntry.Lowercase
import enumeratum._
import tv.teads.github.api.model.JsonEnum

sealed trait MilestoneState extends EnumEntry with Lowercase

case object MilestoneState extends JsonEnum[MilestoneState] {
  override def values: Seq[MilestoneState] = findValues

  case object Closed extends MilestoneState
  case object Open extends MilestoneState
}
