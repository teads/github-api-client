package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class StatusServiceSpec extends BaseSpec {

  "Status Service" should "be able to fetch statuses for a branch" in {

    whenReady(StatusService.fetchStatuses("ebuzzing", "github-api-client", "test-pr")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch combined status for a branch" in {

    whenReady(StatusService.fetchStatus("ebuzzing", "github-api-client", "test-pr")) { list ⇒
      println(list)
      list should not be empty
    }
  }

}
