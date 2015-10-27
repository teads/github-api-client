package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait RateLimitCodec {
  implicit lazy val coreDecoder = deriveFor[Core].decoder
  implicit lazy val resourcesDecoder = deriveFor[Resources].decoder
  implicit lazy val rateLimitDecoder = deriveFor[RateLimit].decoder
}

case class Core(limit: Long, remaining: Long, reset: Long)
case class Resources(core: Core, search: Core)
case class RateLimit(resources: Resources)
