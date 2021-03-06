package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class RepositoryServiceSpec extends BaseSpec {

  "Repository Service" should "be able to fetch all repositories" in {

    whenReady(teadsClient.repositories.fetchAllRepositories) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client Tags" in {

    whenReady(teadsClient.repositories.listTags("github-api-client")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client Languages" in {

    whenReady(teadsClient.repositories.listLanguages("github-api-client")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client Contributors" in {

    whenReady(teadsClient.repositories.listContributors("github-api-client")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client Branches" in {

    whenReady(teadsClient.repositories.listBranches("github-api-client")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client master Branch" in {

    whenReady(teadsClient.repositories.fetchBranch("github-api-client", "master")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch github-api-client repository" in {

    whenReady(teadsClient.repositories.fetch("github-api-client")) { list ⇒
      list should not be empty
    }
  }

}
