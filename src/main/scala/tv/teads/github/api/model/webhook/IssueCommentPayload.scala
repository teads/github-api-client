package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait IssueCommentPayloadCodec {
  self: UserCodec with RepositoryCodec with IssueCodec with CommentCodec ⇒

  implicit lazy val issueCommentPayloadDecoder = deriveDecoder[IssueCommentPayload]
}
case class IssueCommentPayload(
  action:       IssueCommentAction,
  issue:        Issue,
  comment:      Comment,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
