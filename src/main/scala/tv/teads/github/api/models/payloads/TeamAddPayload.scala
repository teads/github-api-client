package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait TeamAddPayloadFormats {
  self: UserFormats with RepositoryFormats with TeamFormats ⇒

  implicit lazy val teamAddPayloadJsonWrite: Write[TeamAddPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[TeamAddPayload, JsObject]
  }

  implicit lazy val teamAddPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, TeamAddPayload]
  }

}
case class TeamAddPayload(
  team:         Team,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
