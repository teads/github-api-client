package tv.teads.github.api.services

import cats.syntax.option._

import scala.concurrent.ExecutionContext.Implicits.global

import tv.teads.github.api.BaseSpec

class ReferenceServiceSpec extends BaseSpec {

  "ReferenceService" should "be able to fetch a single reference" in {

    whenReady(teadsClient.references.fetch("github-api-client", "heads/master")) { ref ⇒
      ref should not be empty
    }
  }

  it should "be able to fetch all branches" in {
    whenReady(teadsClient.references.fetchAll("github-api-client", "heads".some)) { list ⇒
      list should not be empty
    }
  }
}
