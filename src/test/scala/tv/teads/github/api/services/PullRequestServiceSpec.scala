package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class PullRequestServiceSpec extends BaseSpec {

  "Pull Request Service" should "be able to fetch github-api-client Pull Requests" in {

    whenReady(teadsClient.pullRequests.list("github-api-client")) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch a github-api-client Pull Request commits" in {

    whenReady(teadsClient.pullRequests.listCommits("github-api-client", 5)) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch github-api-client Pull Request comments" in {

    whenReady(teadsClient.pullRequests.listComments("github-api-client")) { list ⇒
      list should not be empty
    }
  }

}
