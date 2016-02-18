package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait MemberPayloadCodec {
  self: UserCodec with RepositoryCodec with UserCodec with TeamCodec ⇒

  implicit lazy val memberPayloadDecoder = deriveDecoder[MemberPayload]
}
case class MemberPayload(
  team:         Team,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
