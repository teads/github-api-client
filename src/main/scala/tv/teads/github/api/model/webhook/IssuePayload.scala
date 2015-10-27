package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait IssuePayloadCodec {
  self: UserCodec with RepositoryCodec with IssueCodec with LabelCodec ⇒

  implicit lazy val issuePayloadDecoder = deriveFor[IssuePayload].decoder
}
case class IssuePayload(
  action:     IssueAction,
  issue:      Issue,
  label:      Option[Label],
  repository: Repository,
  sender:     User,
  assignee:   Option[User]
) extends Payload
