package tv.teads.github.api.model.common

import enumeratum.EnumEntry.Lowercase
import enumeratum._

sealed abstract class SortDirection extends EnumEntry with Lowercase

case object SortDirection extends Enum[SortDirection] {
  override def values: Seq[SortDirection] = findValues

  case object Asc extends SortDirection
  case object Desc extends SortDirection
}