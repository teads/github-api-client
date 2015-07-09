package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models._
import play.api.data.mapping._

trait DeploymentPayloadFormats {
  self: UserFormats with RepositoryFormats with TeamFormats ⇒

  implicit lazy val deploymentPayloadJsonWrite: Write[DeploymentPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[DeploymentPayload, JsObject]
  }

  implicit lazy val deploymentPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, DeploymentPayload]
  }

}
case class DeploymentPayload(
  team:         Team,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
