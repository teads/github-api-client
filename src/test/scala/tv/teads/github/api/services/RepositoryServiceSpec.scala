package tv.teads.github.api.services

import scala.concurrent.duration._
import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global


class RepositoryServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Repository Service" should "be able to fetch all repositories" in {

    whenReady(RepositoryService.fetchAllRepositories) { list ⇒
      //      println(list)
      list should not be empty
    }
  }
  it should "be able to fetch service-rtb Pull Requests" in {

    whenReady(RepositoryService.fetchPullRequests("service-rtb")) { list ⇒
      //      println(list)
      list should not be empty
    }
  }
  it should "be able to fetch ccc-service-rtb Tags" in {

    whenReady(RepositoryService.listTags("ccc-service-rtb")) { list ⇒
      //      println(list)
      list should not be empty
    }
  }

}
