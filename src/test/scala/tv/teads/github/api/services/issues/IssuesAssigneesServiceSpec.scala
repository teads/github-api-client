package tv.teads.github.api.services.issues

import tv.teads.github.api.services.AbstractServiceSpec

class IssuesAssigneesServiceSpec extends AbstractServiceSpec {

  "listAvailableAssignees" should {
    "be able to fetch the list of available assignees for github-api-client_test" in {
      whenReady(teadsClient.issues.assignees.listAvailableAssignees("github-api-client_test")) { availableAssignees ⇒
        availableAssignees.value.find(_.login == "pdalpra") should not be empty
      }
    }

    "not be able to fetch the list of available assignees for a non existing repository" in {
      whenReady(teadsClient.issues.assignees.listAvailableAssignees("foobarquz")) { availableAssignees ⇒
        availableAssignees shouldBe empty
      }
    }
  }

  "isAvailableAssignee" should {
    "return true if the user is an available assignee for github-api-client_test" in {
      whenReady(teadsClient.issues.assignees.isAvailableAssignee("github-api-client_test", "pdalpra")) { isAvailableAssignee ⇒
        isAvailableAssignee shouldBe true
      }
    }

    "return false if the user is not an available assignee for github-api-client_test" in {
      whenReady(teadsClient.issues.assignees.isAvailableAssignee("github-api-client", "unknown-user")) { isAvailableAssignee ⇒
        isAvailableAssignee shouldBe false
      }
    }

    "return false if the repository does not exist" in {
      whenReady(teadsClient.issues.assignees.isAvailableAssignee("foobarquz", "pdalpra")) { isAvailableAssignee ⇒
        isAvailableAssignee shouldBe false
      }
    }
  }

}
