package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait WatchPayloadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val watchPayloadDecoder = deriveDecoder[WatchPayload]
}
case class WatchPayload(
  action:     WatchAction,
  repository: Repository,
  sender:     User
) extends Payload
