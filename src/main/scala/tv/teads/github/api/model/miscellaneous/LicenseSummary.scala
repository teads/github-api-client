package tv.teads.github.api.model.miscellaneous

import io.circe.generic.semiauto._

trait LicenseSummaryCodec {
  implicit val licenseSummaryDecoder = deriveDecoder[LicenseSummary]
}
case class LicenseSummary(key: String, name: String, url: String, featured: Boolean)
