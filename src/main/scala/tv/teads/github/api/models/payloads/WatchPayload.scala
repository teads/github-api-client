package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._
import tv.teads.github.api.models.actions.WatchActions.WatchAction

trait WatchPayloadFormats {
  self: UserFormats with RepositoryFormats  =>

  implicit lazy val watchPayloadJsonWrite : Write[WatchPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[WatchPayload, JsObject]
  }

  implicit lazy val watchPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, WatchPayload]
  }

}
case class WatchPayload(
                           action: WatchAction,
                           repository: Repository,
                           sender: User
                           ) extends Payload
