package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models.DeleteRefTypes.DeleteRefType
import tv.teads.github.api.models._
import play.api.data.mapping._

trait DeletePayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val deletePayloadJsonWrite: Write[DeletePayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[DeletePayload, JsObject]
  }

  implicit lazy val deletePayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "ref").read[String] ~
      (__ \ "ref_type").read[DeleteRefType] ~
      (__ \ "pusher_type").read[String] ~
      (__ \ "repository").read[Repository] ~
      (__ \ "organization").read[Option[User]] ~
      (__ \ "sender").read[User]
    )(DeletePayload.apply _)
  }

}
case class DeletePayload(
  ref:          String,
  refType:      DeleteRefType,
  pusherType:   String,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
