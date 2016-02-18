package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait MembershipPayloadCodec {
  self: UserCodec with RepositoryCodec with TeamCodec ⇒

  implicit lazy val membershipPayloadDecoder = deriveDecoder[MembershipPayload]
}
case class MembershipPayload(
  action:       String,
  scope:        String,
  member:       User,
  sender:       User,
  team:         Team,
  organization: User
) extends Payload
