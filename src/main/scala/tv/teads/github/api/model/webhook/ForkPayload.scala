package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait ForkPayloadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val forkPayloadDecoder = deriveDecoder[ForkPayload]
}
case class ForkPayload(
  forkee:       Repository,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
