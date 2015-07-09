package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.actions.IssueActions
import IssueActions.IssueAction
import tv.teads.github.api.models._
import play.api.data.mapping._

trait IssuePayloadFormats {
  self: UserFormats with RepositoryFormats with IssueFormats with LabelFormats ⇒

  implicit lazy val issuePayloadJsonWrite: Write[IssuePayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[IssuePayload, JsObject]
  }

  implicit lazy val issuePayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, IssuePayload]
  }

}
case class IssuePayload(
  action:     IssueAction,
  issue:      Issue,
  label:      Option[Label],
  repository: Repository,
  sender:     User,
  assignee:   Option[User]
) extends Payload
