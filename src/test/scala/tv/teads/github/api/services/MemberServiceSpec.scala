package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class MemberServiceSpec extends BaseSpec {

  "Member Service" should "be able to fetch an organization members (ebuzzing) " in {

    whenReady(MemberService.fetchOrgMembers("ebuzzing")) { org ⇒
      org should not be empty
    }
  }
  it should "be able to fetch default (configured) organization members" in {

    whenReady(MemberService.fetchDefaultOrgMembers) { org ⇒
      org should not be empty
    }
  }

}
