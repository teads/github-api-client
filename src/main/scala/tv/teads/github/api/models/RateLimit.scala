package tv.teads.github.api.models

import org.joda.time.DateTime

object RateLimit {

  case class Core(
                   limit: Long,
                   remaining: Long,
                   reset: Long
                   ) {
    override def toString = s"rest $remaining of $limit. Reset at ${new DateTime(reset * 1000).toString("yyyy-MM-dd HH:mm:ss")}"
  }

  case class Resources(
                        core: Core,
                        search: Core
                        )

  case class RateLimit(
                        resources: Resources,
                        rate: Core
                        )

}
