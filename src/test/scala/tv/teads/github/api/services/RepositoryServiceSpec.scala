package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class RepositoryServiceSpec extends BaseSpec {

  "Repository Service" should "be able to fetch all repositories" in {

    whenReady(ebuzzingClient.repositories.fetchAllRepositories) { list ⇒
      list should not be empty
      list.size should be > 100
    }
  }
  it should "be able to fetch ccc-service-rtb Tags" in {

    whenReady(ebuzzingClient.repositories.listTags("ccc-service-rtb")) { list ⇒
      list should not be empty
    }
  }

}
