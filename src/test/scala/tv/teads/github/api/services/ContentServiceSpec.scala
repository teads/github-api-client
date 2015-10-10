package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class ContentServiceSpec extends BaseSpec {

  "Content Service" should "be able to fetch readme of repository ebuzzing/github-api-client" in {

    whenReady(ContentService.fetchReadme("ebuzzing", "github-api-client")) { file ⇒
      file should not be empty
    }
  }
  it should "be able to fetch readme of repository github-api-client" in {

    whenReady(ContentService.fetchReadmeDefaultOrg("github-api-client")) { file ⇒
      file should not be empty
    }
  }
  it should "be able to fetch the Content of the README file on default branch of ebuzzing/github-api-client" in {

    whenReady(ContentService.fetchFile("ebuzzing", "github-api-client", "README.md")) { file ⇒
      file should not be empty
    }
  }
  it should "be able to fetch the Content of the README file on default branch of github-api-client" in {

    whenReady(ContentService.fetchFileDefaultOrg("github-api-client", "README.md")) { file ⇒
      file should not be empty
    }
  }

}
