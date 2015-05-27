package tv.teads.github.api.services

import scala.concurrent.duration._
import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global


class GithubServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Github Service" should "be able to fetch all repositories" in {

    whenReady(GithubService.fetchAllRepositories) { list ⇒
//      println(list)
      list should not be empty
    }
  }

}
