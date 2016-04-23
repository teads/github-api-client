package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class StatusServiceSpec extends BaseSpec {

  "Status Service" should "be able to fetch statuses for a branch" in {

    whenReady(teadsClient.statuses.list("github-api-client", "test-pr")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch combined status for a branch" in {

    whenReady(teadsClient.statuses.get("github-api-client", "test-pr")) { list ⇒
      list should not be empty
    }
  }

}
