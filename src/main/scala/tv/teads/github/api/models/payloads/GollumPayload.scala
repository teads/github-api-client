package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models._
import play.api.data.mapping._

trait GollumPayloadFormats {
  self: UserFormats with RepositoryFormats with TeamFormats ⇒

  implicit lazy val gollumPayloadJsonWrite: Write[GollumPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[GollumPayload, JsObject]
  }

  implicit lazy val gollumPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, GollumPayload]
  }

}
case class GollumPayload(
  team:         Team,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
