package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class RepositoryServiceSpec extends BaseSpec {

  "Repository Service" should "be able to fetch all repositories" in {

    whenReady(RepositoryService.fetchAllRepositories) { list ⇒
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
