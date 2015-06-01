package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.models.Issue
import tv.teads.github.api.services.IssueService.IssueParam

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class IssueServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Issue Service" should "be able to create an issue" in {


    val repository = "github-hooks"
    val issue = IssueParam(title="IssueServiceSpec.createTest", body = Some("html body with some `markup`"))

    whenReady(IssueService.create(repository, issue)) { res ⇒
      //      println(list)
      res should not be empty
    }
  }

}
