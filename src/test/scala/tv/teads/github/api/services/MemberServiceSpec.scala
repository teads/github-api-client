package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class MemberServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

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
