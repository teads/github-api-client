package tv.teads.github.api.services

import tv.teads.github.api.filters._
import tv.teads.github.api.BaseSpec
import tv.teads.github.api.services.IssueService._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.ExecutionContext.Implicits.global

class IssueServiceSpec extends BaseSpec {

  "Issue Service" should "be able to create an issue, close it, re-open it and finally close it" in {

    val repository = "github-api-client"
    val issue = IssueParam(title = "IssueServiceSpec.createTest", body = Some("html body with some `markup`"))

    whenReady(teadsClient.issues.create(repository, issue)) { res ⇒
      res should not be empty
      val issueNumber = res.get.number

      whenReady(teadsClient.issues.close(repository, issueNumber, issue)) { res1 ⇒
        res1 should not be empty

        whenReady(teadsClient.issues.open(repository, issueNumber, issue)) { res2 ⇒
          res2 should not be empty

          whenReady(teadsClient.issues.close(repository, issueNumber, issue)) { res3 ⇒
            res3 should not be empty
          }
        }
      }
    }
  }

  it should "transform an IssueFilter with default values into a Map" in {
    val filter = IssueFilter()
    val expected = Map("state" → IssueState.open.toString, "sort" → Sort.created.toString, "direction" → Direction.desc.toString)
    filter.toMapStringified should contain theSameElementsAs expected
  }

  it should "be able to fetch github-api-client Open issues" in {
    val filter = IssueFilter(state = Some(IssueState.open))

    whenReady(teadsClient.issues.byRepository("github-api-client", filter)) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch github-api-client Pull Request (as issue) comments" in {

    whenReady(teadsClient.issues.fetchIssueComments("github-api-client", 115)) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch github-api-client issues comments" in {

    whenReady(teadsClient.issues.fetchComments("github-api-client")) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch a specific github-api-client issue comment" in {

    val commentId = 150513228

    whenReady(teadsClient.issues.fetchComment("github-api-client", commentId)) { opt ⇒
      opt should not be empty
    }
  }

}
