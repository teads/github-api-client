package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.filters.common.Directions.Direction
import tv.teads.github.api.filters.common.States.State
import tv.teads.github.api.services.IssueService.{Sort, IssueFilter, IssueParam}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class IssueServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Issue Service" should "be able to create an issue, close it, re-open it and finally close it" in {


    val repository = "github-hooks"
    val issue = IssueParam(title = "IssueServiceSpec.createTest", body = Some("html body with some `markup`"))

    whenReady(IssueService.create(repository, issue)) { res ⇒
      res should not be empty
      val issueNumber = res.get.number

      whenReady(IssueService.close(repository, issueNumber, issue)) { res1 ⇒
        res1 should not be empty

        whenReady(IssueService.open(repository, issueNumber, issue)) { res2 ⇒
          res2 should not be empty

          whenReady(IssueService.close(repository, issueNumber, issue)) { res3 ⇒
            res3 should not be empty
          }
        }
      }
    }
  }

  it should "transform an IssueFilter with default values into a Map" in {
    val filter = IssueFilter()
    val expected = Map("state" -> State.open.toString, "sort" -> Sort.created.toString, "direction" -> Direction.desc.toString)
    IssueService.filterToMap(filter) should contain theSameElementsAs expected
  }

}
