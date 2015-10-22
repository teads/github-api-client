package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._
import tv.teads.github.api.models.actions.ReleaseActions.ReleaseAction

trait ReleasePayloadFormats {
  self: UserFormats with RepositoryFormats with ReleaseFormats ⇒

  implicit lazy val releasePayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, ReleasePayload]
  }

}
case class ReleasePayload(
  action:     ReleaseAction,
  release:    Release,
  repository: Repository,
  sender:     User
) extends Payload
