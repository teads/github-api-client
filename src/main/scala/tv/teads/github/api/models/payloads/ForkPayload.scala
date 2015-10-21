package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait ForkPayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val forkPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, ForkPayload]
  }

}
case class ForkPayload(
  forkee:       Repository,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
