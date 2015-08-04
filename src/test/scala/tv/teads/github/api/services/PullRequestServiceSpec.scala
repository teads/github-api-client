package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class PullRequestServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Pull Request Service" should "be able to fetch github-hooks Pull Requests" in {

    whenReady(PullRequestService.fetchPullRequests("github-hooks")) { list ⇒
      //      println(list)
      list should not be empty
    }
  }

  it should "be able to fetch a github-hooks Pull Request commits" in {

    whenReady(PullRequestService.fetchCommits("github-hooks", 5)) { list ⇒
      //      println(list)
      list should not be empty
    }
  }

}
