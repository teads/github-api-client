package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.data.mapping.{Rule, Write}
import play.api.libs.json.{JsObject, JsValue}

trait CoreFormats {
  implicit lazy val coreJsonWrite: Write[Core, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Core, JsObject]
  }

  implicit lazy val coreJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Core]
  }

}

trait ResourcesFormats {
  self: CoreFormats ⇒
  implicit lazy val resourcesJsonWrite: Write[Resources, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Resources, JsObject]
  }

  implicit lazy val resourcesJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Resources]
  }

}

trait RateLimitFormats {
  self: CoreFormats with ResourcesFormats ⇒
  implicit lazy val rateLimitJsonWrite: Write[RateLimit, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[RateLimit, JsObject]
  }

  implicit lazy val rateLimitJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, RateLimit]
  }

}

case class Core(
    limit:     Long,
    remaining: Long,
    reset:     Long
) {
  override def toString = s"rest $remaining of $limit. Reset at ${new DateTime(reset * 1000).toString("yyyy-MM-dd HH:mm:ss")}"
}

case class Resources(
  core:   Core,
  search: Core
)

case class RateLimit(
  resources: Resources
)

