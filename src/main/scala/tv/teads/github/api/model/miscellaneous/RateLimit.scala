package tv.teads.github.api.model.miscellaneous

case class RateLimit(resources: Resources, rate: Limits)
case class Resources(core: Limits, search: Limits)
case class Limits(limit: Int, remaining: Int, reset: Long)
