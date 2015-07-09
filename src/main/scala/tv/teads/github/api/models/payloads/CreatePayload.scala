package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models.CreateRefTypes.CreateRefType
import tv.teads.github.api.models._
import play.api.data.mapping._

trait CreatePayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val createPayloadJsonWrite: Write[CreatePayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[CreatePayload, JsObject]
  }

  implicit lazy val createPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, CreatePayload]
  }

}

case class CreatePayload(
  ref:           Option[String],
  ref_type:      CreateRefType,
  master_branch: String,
  description:   Option[String],
  pusher_type:   String,
  repository:    Repository,
  organization:  Option[User],
  sender:        User
) extends Payload
