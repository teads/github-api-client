package tv.teads.github.api.model.repositories

case class YearlyWeeklyCommitCount(all: List[Int], owner: List[Int]) {
  val nonOwners = (all zip owner).map { case (weekAll, weekOwner) ⇒ weekAll - weekOwner }
}
