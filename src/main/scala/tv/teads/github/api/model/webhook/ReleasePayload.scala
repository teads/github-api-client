package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait ReleasePayloadCodec {
  self: UserCodec with RepositoryCodec with ReleaseCodec ⇒

  implicit lazy val releasePayloadDecoder = deriveDecoder[ReleasePayload]
}
case class ReleasePayload(
  action:     ReleaseAction,
  release:    Release,
  repository: Repository,
  sender:     User
) extends Payload
