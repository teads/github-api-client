package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PublicPayloadFormats {
  self: UserFormats with RepositoryFormats  with TeamFormats =>

  implicit lazy val  publicPayloadJsonWrite : Write[PublicPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PublicPayload, JsObject]
  }

  implicit lazy val  publicPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._// let's no leak implicits everywhere
    Rule.gen[JsValue, PublicPayload]
  }

}
case class PublicPayload(
                           team: Team,
                           repository: Repository,
                           organization: Option[User],
                           sender: User
                           ) extends Payload
