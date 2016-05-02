package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.model.miscellaneous.GithubApiInfo
import tv.teads.github.api.services.AbstractServiceSpec

class MetaServiceSpec extends AbstractServiceSpec {

  "get" should {
    "be able to fetch Github API information" in {
      whenReady(teadsClient.miscellaneous.meta.get) { meta ⇒
        // Only check that deserialization worked
        meta shouldBe a[GithubApiInfo]
      }
    }
  }

}
