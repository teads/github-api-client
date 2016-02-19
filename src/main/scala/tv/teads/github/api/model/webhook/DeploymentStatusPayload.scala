package tv.teads.github.api.model.webhook

import io.circe.generic.semiauto._

import tv.teads.github.api.model._

trait DeploymentStatusPayloadCodec {
  self: DeploymentCodec with RepositoryCodec with DeploymentStatusCodec ⇒

  implicit lazy val deploymentStatusPayloadDecoder = deriveDecoder[DeploymentStatusPayload]
}
case class DeploymentStatusPayload(
  deployment_status: DeploymentStatus,
  repository:        Repository,
  deployment:        Deployment
) extends Payload
