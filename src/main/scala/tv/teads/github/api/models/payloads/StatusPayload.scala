package tv.teads.github.api.models.payloads

import org.joda.time.DateTime
import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.StatusStates.StatusState
import tv.teads.github.api.models._
import play.api.data.mapping._

trait StatusPayloadFormats {
  self: UserFormats with RepositoryFormats with GHCommitFormats with BranchFormats ⇒

  implicit lazy val statusPayloadJsonWrite: Write[StatusPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[StatusPayload, JsObject]
  }

  implicit lazy val statusPayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "id").read[Long] ~
      (__ \ "sha").read[String] ~
      (__ \ "name").read[String] ~
      (__ \ "target_url").read[Option[String]] ~
      (__ \ "context").read[String] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "state").read[StatusState] ~
      (__ \ "commit").read[GHCommit] ~
      (__ \ "branches").read[List[Branch]] ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "repository").read[Repository] ~
      (__ \ "sender").read[User]
    )(StatusPayload.apply _)
  }

}
case class StatusPayload(
  id:          Long,
  sha:         String,
  name:        String,
  targetUrl:  Option[String],
  context:     String,
  description: Option[String],
  state:       StatusState,
  commit:      GHCommit,
  branches:    List[Branch],
  createdAt:  DateTime,
  updatedAt:  DateTime,
  repository:  Repository,
  sender:      User
) extends Payload
