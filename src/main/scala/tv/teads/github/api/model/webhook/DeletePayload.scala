package tv.teads.github.api.model.webhook

import io.circe._

import tv.teads.github.api.model._

trait DeletePayloadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val deletePayloadDecoder = Decoder.instance { cursor ⇒
    for {
      ref ← cursor.downField("ref").as[String]
      refType ← cursor.downField("ref_type").as[DeleteRefType]
      pusherType ← cursor.downField("pusher_type").as[String]
      repository ← cursor.downField("repository").as[Repository]
      organization ← cursor.downField("organization").as[Option[User]]
      sender ← cursor.downField("sender").as[User]
    } yield DeletePayload(ref, refType, pusherType, repository, organization, sender)
  }
}
case class DeletePayload(
  ref:          String,
  refType:      DeleteRefType,
  pusherType:   String,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
