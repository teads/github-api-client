package tv.teads.github.api.models.payloads

import play.api.libs.json.JsValue
import tv.teads.github.api.models._
import play.api.data.mapping._
import tv.teads.github.api.models.actions.WatchActions.WatchAction

trait WatchPayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val watchPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, WatchPayload]
  }

}
case class WatchPayload(
  action:     WatchAction,
  repository: Repository,
  sender:     User
) extends Payload
