package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait MembershipPayloadFormats {
  self: UserFormats with RepositoryFormats with TeamFormats ⇒

  implicit lazy val membershipPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, MembershipPayload]
  }

}
case class MembershipPayload(
  action:       String,
  scope:        String,
  member:       User,
  sender:       User,
  team:         Team,
  organization: User
) extends Payload
