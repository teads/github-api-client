package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class HookServiceSpec extends BaseSpec {

  "Hook Service" should "be able to fetch an organization hooks" in {

    whenReady(teadsClient.hooks.listByOrganization) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch repository github-api-client hooks" in {
    whenReady(teadsClient.hooks.listByRepository("github-api-client")) { list ⇒
      list should not be empty
    }
  }
}
