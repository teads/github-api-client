package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models._
import play.api.data.mapping._

trait DeploymentStatusPayloadFormats {
  self: UserFormats with RepositoryFormats with TeamFormats ⇒

  implicit lazy val deploymentStatusPayloadJsonWrite: Write[DeploymentStatusPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[DeploymentStatusPayload, JsObject]
  }

  implicit lazy val deploymentStatusPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, DeploymentStatusPayload]
  }

}
case class DeploymentStatusPayload(
  team:         Team,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
