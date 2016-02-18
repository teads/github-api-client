package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait CommitCommentPayloadCodec {
  self: UserCodec with CommitCommentCodec with RepositoryCodec ⇒

  implicit lazy val commitCommentPayloadDecoder = deriveDecoder[CommitCommentPayload]
}
case class CommitCommentPayload(
  comment:      CommitComment,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
