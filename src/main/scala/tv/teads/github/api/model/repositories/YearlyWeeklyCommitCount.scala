package tv.teads.github.api.model.repositories

import io.circe.generic.semiauto._

trait YearlyWeeklyCommitCountCodec {
  implicit val yearlyWeeklyCommitCountDecoder = deriveDecoder[YearlyWeeklyCommitCount]
}
case class YearlyWeeklyCommitCount(all: List[Int], owner: List[Int]) {
  val nonOwners = (all zip owner).map { case (weekAll, weekOwner) ⇒ weekAll - weekOwner }
}
