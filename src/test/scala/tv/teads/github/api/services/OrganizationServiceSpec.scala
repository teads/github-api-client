package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class OrganizationServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Organization Service" should "be able to fetch an organization (ebuzzing) " in {

    whenReady(OrganizationService.fetchOrg("ebuzzing")) { org ⇒
      org should not be empty
      org.get.name === "ebuzzing"
    }
  }
  it should "be able to fetch default (configured) organization" in {

    whenReady(OrganizationService.fetchDefaultOrg) { org ⇒
      org should not be empty
      org.get.name === "ebuzzing"
    }
  }

  it should "be able to fetch user BobTheBot-teads' organization" in {

    whenReady(OrganizationService.fetchUserOrgs("BobTheBot-teads")) { list ⇒
      list should not be empty
    }
  }

}
