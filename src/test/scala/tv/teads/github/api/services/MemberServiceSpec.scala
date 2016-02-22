package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class MemberServiceSpec extends BaseSpec {

  "Member Service" should "be able to fetch the client's organization members" in {

    whenReady(teadsClient.members.listOrganizationMembers) { org ⇒
      org should not be empty
    }
  }
}
