package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait RateLimitCodec {
  implicit lazy val coreDecoder = deriveDecoder[Core]
  implicit lazy val resourcesDecoder = deriveDecoder[Resources]
  implicit lazy val rateLimitDecoder = deriveDecoder[RateLimit]
}

case class Core(limit: Long, remaining: Long, reset: Long)
case class Resources(core: Core, search: Core)
case class RateLimit(resources: Resources)
