package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe.Decoder
import tv.teads.github.api.json.ZonedDateTimeCodec

trait DeploymentStatusCodec {
  self: DeploymentCodec with UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val deploymentStatusDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      id ← cursor.downField("id").as[Long]
      state ← cursor.downField("state").as[DeploymentStatusState]
      creator ← cursor.downField("creator").as[User]
      description ← cursor.downField("description").as[Option[String]]
      targetUrl ← cursor.downField("target_url").as[Option[String]]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      deploymentUrl ← cursor.downField("deployment_url").as[String]
      repositoryUrl ← cursor.downField("repository_url").as[String]
    } yield DeploymentStatus(url, id, state, creator, description, targetUrl, createdAt, updatedAt, deploymentUrl, repositoryUrl)
  }
}

case class DeploymentStatus(
  url:           String,
  id:            Long,
  state:         DeploymentStatusState,
  creator:       User,
  description:   Option[String],
  targetUrl:     Option[String],
  createdAt:     ZonedDateTime,
  updatedAt:     ZonedDateTime,
  deploymentUrl: String,
  repositoryUrl: String
)
