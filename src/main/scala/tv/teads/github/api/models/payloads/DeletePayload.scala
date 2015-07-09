package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.DeleteRefTypes.DeleteRefType
import tv.teads.github.api.models._
import play.api.data.mapping._

trait DeletePayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val deletePayloadJsonWrite: Write[DeletePayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[DeletePayload, JsObject]
  }

  implicit lazy val deletePayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, DeletePayload]
  }

}
case class DeletePayload(
  ref:          String,
  ref_type:     DeleteRefType,
  pusher_type:  String,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
