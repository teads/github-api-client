package tv.teads.github.api.model

import io.circe._

trait OrganizationMembershipCodec {
  implicit lazy val organizationMembershipDecoder = Decoder.instance { cursor ⇒
    for {
      role ← cursor.downField("role").as[String]
      state ← cursor.downField("state").as[String]
    } yield OrganizationMembership(role, state)
  }
}

case class OrganizationMembership(role: String, state: String)

