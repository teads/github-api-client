package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait RepositoryPayloadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val repositoryPayloadDecoder = deriveDecoder[RepositoryPayload]
}
case class RepositoryPayload(
  action:       RepositoryAction,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
