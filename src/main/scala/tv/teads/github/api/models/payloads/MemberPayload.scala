package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait MemberPayloadFormats {
  self: UserFormats with RepositoryFormats with UserFormats with TeamFormats =>

  implicit lazy val  memberPayloadJsonWrite : Write[MemberPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[MemberPayload, JsObject]
  }

  implicit lazy val  memberPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._// let's no leak implicits everywhere
    Rule.gen[JsValue, MemberPayload]
  }

}
case class MemberPayload(
                           team: Team,
                           repository: Repository,
                           organization: Option[User],
                           sender: User
                           ) extends Payload
