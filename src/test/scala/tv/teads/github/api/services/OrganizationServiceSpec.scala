package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class OrganizationServiceSpec extends BaseSpec {

  "Organization Service" should "be able to fetch the client organization" in {

    whenReady(ebuzzingClient.organizations.fetchOrg) { org ⇒
      org should not be empty
      org.get.name === "ebuzzing"
    }
  }

  it should "be able to fetch user BobTheBot-teads' organization" in {

    whenReady(ebuzzingClient.organizations.fetchUserOrgs("BobTheBot-teads")) { list ⇒
      list should not be empty
    }
  }

}
