package tv.teads.github.api.model.miscellaneous

import io.circe.generic.semiauto._

trait RateLimitCodec {
  implicit val limitsDecoder = deriveDecoder[Limits]
  implicit val resourcesDecoder = deriveDecoder[Resources]
  implicit val rateLimitDecoder = deriveDecoder[RateLimit]
}
case class RateLimit(resources: Resources, rate: Limits)
case class Resources(core: Limits, search: Limits)
case class Limits(limit: Int, remaining: Int, reset: Long)
