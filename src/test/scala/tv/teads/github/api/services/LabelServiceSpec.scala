package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class LabelServiceSpec extends BaseSpec {

  "Label Service" should "be able to fetch a repository labels" in {

    whenReady(teadsClient.labels.list("github-api-client")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch a repository label" in {

    whenReady(teadsClient.labels.get("github-api-client", "bug")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch an issue labels" in {

    whenReady(teadsClient.labels.listOnIssue("github-api-client", 233)) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch a milestone labels" in {

    whenReady(teadsClient.labels.listInMilestone("github-api-client", 1)) { list ⇒
      list should not be empty
    }
  }

}
