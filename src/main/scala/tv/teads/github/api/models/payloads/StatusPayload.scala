package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.StatusStates.StatusState
import tv.teads.github.api.models._
import play.api.data.mapping._

trait StatusPayloadFormats {
  self: UserFormats with RepositoryFormats with GHCommitFormats with BranchFormats ⇒

  implicit lazy val statusPayloadJsonWrite: Write[StatusPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[StatusPayload, JsObject]
  }

  implicit lazy val statusPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, StatusPayload]
  }

}
case class StatusPayload(
  id:          Long,
  sha:         String,
  name:        String,
  target_url:  Option[String],
  context:     String,
  description: Option[String],
  state:       StatusState,
  commit:      GHCommit,
  branches:    List[Branch],
  created_at:  String,
  updated_at:  String,
  repository:  Repository,
  sender:      User
) extends Payload
