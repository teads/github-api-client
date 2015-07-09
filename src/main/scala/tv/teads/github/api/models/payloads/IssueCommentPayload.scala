package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.actions.IssueCommentActions
import IssueCommentActions.IssueCommentAction
import tv.teads.github.api.models._
import play.api.data.mapping._

trait IssueCommentPayloadFormats {
  self: UserFormats with RepositoryFormats with IssueFormats with CommentFormats ⇒

  implicit lazy val issueCommentPayloadJsonWrite: Write[IssueCommentPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[IssueCommentPayload, JsObject]
  }

  implicit lazy val issueCommentPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, IssueCommentPayload]
  }

}
case class IssueCommentPayload(
  action:       IssueCommentAction,
  issue:        Issue,
  comment:      Comment,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
