package tv.teads.github.api.models.payloads

import tv.teads.github.api.models._
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait CommitCommentPayloadFormats {
  self: UserFormats with CommitCommentFormats with RepositoryFormats ⇒

  implicit lazy val commitCommentPayloadJsonWrite: Write[CommitCommentPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[CommitCommentPayload, JsObject]
  }

  implicit lazy val commitCommentPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, CommitCommentPayload]
  }

}
case class CommitCommentPayload(
  comment:      CommitComment,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
