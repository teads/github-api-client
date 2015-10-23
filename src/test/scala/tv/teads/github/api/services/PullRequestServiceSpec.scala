package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.services.PullRequestService.PullRequestFilter

import scala.concurrent.ExecutionContext.Implicits.global

class PullRequestServiceSpec extends BaseSpec {

  "Pull Request Service" should "be able to fetch github-api-client Pull Requests" in {

    whenReady(PullRequestService.fetchPullRequests("github-api-client", PullRequestFilter())) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch a github-api-client Pull Request commits" in {

    whenReady(PullRequestService.fetchCommits("github-api-client", 5)) { list ⇒
      list should not be empty
    }
  }

}
