package tv.teads.github.api.model.webhook

import io.circe._

import tv.teads.github.api.model._

trait CreatePayloadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val createPayloadDecoder = Decoder.instance { cursor ⇒
    for {
      ref ← cursor.downField("ref").as[Option[String]]
      refType ← cursor.downField("ref_type").as[CreateRefType]
      masterBranch ← cursor.downField("master_branch").as[String]
      description ← cursor.downField("description").as[Option[String]]
      pusherType ← cursor.downField("pusher_type").as[String]
      repository ← cursor.downField("repository").as[Repository]
      organization ← cursor.downField("organization").as[Option[User]]
      sender ← cursor.downField("sender").as[User]
    } yield CreatePayload(ref, refType, masterBranch, description, pusherType, repository, organization, sender)
  }
}

case class CreatePayload(
  ref:          Option[String],
  refType:      CreateRefType,
  masterBranch: String,
  description:  Option[String],
  pusherType:   String,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
