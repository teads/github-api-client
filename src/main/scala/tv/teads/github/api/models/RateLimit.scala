package tv.teads.github.api.models

import play.api.data.mapping.Rule
import play.api.libs.json.JsValue

trait CoreFormats {
  implicit lazy val coreJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Core]
  }

}

trait ResourcesFormats {
  self: CoreFormats ⇒

  implicit lazy val resourcesJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Resources]
  }

}

trait RateLimitFormats {
  self: CoreFormats with ResourcesFormats ⇒

  implicit lazy val rateLimitJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, RateLimit]
  }

}

case class Core(
  limit:     Long,
  remaining: Long,
  reset:     Long
)

case class Resources(
  core:   Core,
  search: Core
)

case class RateLimit(
  resources: Resources
)

