package tv.teads.github.api.services.issues

import cats.syntax.option._
import tv.teads.github.api.model.issues.{ListMilestonesParameters, MilestoneStateParameter}
import tv.teads.github.api.services.AbstractServiceSpec

class IssuesMilestonesServiceSpec extends AbstractServiceSpec {

  "listAll" should {
    "be able to fetch open milestones on github-api-client (no filters)" in {
      whenReady(teadsClient.issues.milestones.listAll("github-api-client_test")) { milestones ⇒
        println(milestones.value.head.title)
        milestones.value.find(_.title == "Test milestone") should not be empty
      }
    }

    "be able to fetch only closed milestones on github-api-client" in {
      val filter = ListMilestonesParameters(state = MilestoneStateParameter.Closed.some)
      whenReady(teadsClient.issues.milestones.listAll("github-api-client_test", filter)) { milestones ⇒
        milestones.value.find(_.title == "Test milestone") shouldBe empty
      }
    }

    "not be able to fetch milestones for a non existing repository" in {
      whenReady(teadsClient.issues.milestones.listAll("foobarquz")) { milestones ⇒
        milestones shouldBe empty
      }
    }
  }

  "get" should {
    "be able to fetch an existing milestone on github-api-client_test" in {
      whenReady(teadsClient.issues.milestones.get("github-api-client_test", 1)) { milestone ⇒
        milestone.value.title shouldBe "Test milestone"
      }
    }

    "not be able to fetch an non existing milestone on github-api-client_test" in {
      whenReady(teadsClient.issues.milestones.get("github-api-client_test", -1)) { milestone ⇒
        milestone shouldBe empty
      }
    }

    "not be able to fetch an non existing milestone on à non existing repository" in {
      whenReady(teadsClient.issues.milestones.get("foobarquz", 1)) { milestone ⇒
        milestone shouldBe empty
      }
    }
  }

  // TODO
  /*"create" should {
    "" in {
    }
  }

  "edit" should {
    "" in {
    }
  }

  "delete" should {
    "" in {
    }
  }*/

}
