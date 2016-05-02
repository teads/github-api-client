package tv.teads.github.api.model.issues

import enumeratum._, enumeratum.EnumEntry._
import tv.teads.github.api.model.common.SortDirection

sealed trait MilestoneStateParameter extends EnumEntry with Lowercase
object MilestoneStateParameter extends Enum[MilestoneStateParameter] {
  override def values: Seq[MilestoneStateParameter] = findValues

  case object Open extends MilestoneStateParameter
  case object Closed extends MilestoneStateParameter
  case object All extends MilestoneStateParameter
}

sealed trait MilestoneSortParameter extends EnumEntry with Snakecase
object MilestoneSortParameter extends Enum[MilestoneSortParameter] {
  override def values: Seq[MilestoneSortParameter] = findValues

  case object DueOn extends MilestoneSortParameter
  case object Completeness extends MilestoneSortParameter
}

case class ListMilestonesParameters(
  state:     Option[MilestoneStateParameter] = None,
  sort:      Option[MilestoneSortParameter]  = None,
  direction: Option[SortDirection]           = None
)
