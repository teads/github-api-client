package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.services.CommitService.CommitFilter

import scala.concurrent.ExecutionContext.Implicits.global

class CommitServiceSpec extends BaseSpec {

  "Commit Service" should "be able to fetch github-api-client commits" in {

    whenReady(teadsClient.commits.list("github-api-client", CommitFilter(sha = Some("master")))) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client commit cd0a8c2d4093ae78fa8587106622740249674bdc" in {

    whenReady(teadsClient.commits.fetch("github-api-client", "cd0a8c2d4093ae78fa8587106622740249674bdc")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client compare cd0a8c2d4093ae78fa8587106622740249674bdc " in {

    whenReady(teadsClient.commits.compare("github-api-client", "cd0a8c2d4093ae78fa8587106622740249674bdc", "ac1f3a77631e75999ec50e599505f01494138592")) { list ⇒
      list should not be empty
    }
  }

}
