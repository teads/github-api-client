package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PageBuildPayloadFormats {
  self: UserFormats with RepositoryFormats  with TeamFormats =>

  implicit lazy val  pageBuildPayloadJsonWrite : Write[PageBuildPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PageBuildPayload, JsObject]
  }

  implicit lazy val  pageBuildPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._// let's no leak implicits everywhere
    Rule.gen[JsValue, PageBuildPayload]
  }

}
case class PageBuildPayload(
                           team: Team,
                           repository: Repository,
                           organization: Option[User],
                           sender: User
                           ) extends Payload
