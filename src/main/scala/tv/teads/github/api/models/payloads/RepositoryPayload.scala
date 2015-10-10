package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models.actions.RepositoryActions
import RepositoryActions.RepositoryAction
import tv.teads.github.api.models._
import play.api.data.mapping._

trait RepositoryPayloadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val repositoryPayloadJsonWrite: Write[RepositoryPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[RepositoryPayload, JsObject]
  }

  implicit lazy val repositoryPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, RepositoryPayload]
  }

}
case class RepositoryPayload(
  action:       RepositoryAction,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
