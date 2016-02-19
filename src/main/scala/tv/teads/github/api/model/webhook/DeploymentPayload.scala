package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait DeploymentPayloadCodec {
  self: UserCodec with RepositoryCodec with DeploymentCodec ⇒

  implicit lazy val deploymentPayloadDecoder = deriveDecoder[DeploymentPayload]
}
case class DeploymentPayload(
  deployment: Deployment,
  repository: Repository
) extends Payload
