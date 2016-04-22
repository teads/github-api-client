package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.model.miscellaneous.RateLimit
import tv.teads.github.api.services.AbstractServiceSpec

class RateLimitServiceSpec extends AbstractServiceSpec {

  "get" should {
    "be able to fetch the current rate Limit" in {
      whenReady(teadsClient.miscellaneous.rateLimit.get) { rateLimit ⇒
        // Only check that deserialization worked
        rateLimit shouldBe a[RateLimit]
      }
    }
  }
}
