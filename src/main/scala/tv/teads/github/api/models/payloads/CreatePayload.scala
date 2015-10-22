package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models.CreateRefTypes.CreateRefType
import tv.teads.github.api.models._
import play.api.data.mapping._

trait CreatePayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val createPayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "ref").read[Option[String]] ~
      (__ \ "ref_type").read[CreateRefType] ~
      (__ \ "master_branch").read[String] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "pusher_type").read[String] ~
      (__ \ "repository").read[Repository] ~
      (__ \ "organization").read[Option[User]] ~
      (__ \ "sender").read[User]
    )(CreatePayload.apply _)
  }

}

case class CreatePayload(
  ref:          Option[String],
  refType:      CreateRefType,
  masterBranch: String,
  description:  Option[String],
  pusherType:   String,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
