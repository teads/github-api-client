package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe.{Json, Decoder}
import tv.teads.github.api.json.ZonedDateTimeCodec

trait DeploymentCodec {
  self: UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val deploymentDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      id ← cursor.downField("id").as[Long]
      sha ← cursor.downField("sha").as[String]
      ref ← cursor.downField("ref").as[String]
      task ← cursor.downField("task").as[String]
      payload ← cursor.downField("payload").as[Option[Json]]
      environment ← cursor.downField("environment").as[String]
      description ← cursor.downField("description").as[Option[String]]
      creator ← cursor.downField("creator").as[User]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      statusesUrl ← cursor.downField("statuses_url").as[String]
      repositoryUrl ← cursor.downField("repository_url").as[String]
    } yield Deployment(url, id, sha, ref, task, payload, environment, description, creator, createdAt, updatedAt, statusesUrl, repositoryUrl)
  }
}

case class Deployment(
  url:           String,
  id:            Long,
  sha:           String,
  ref:           String,
  task:          String,
  payload:       Option[Json],
  environment:   String,
  description:   Option[String],
  creator:       User,
  createdAt:     ZonedDateTime,
  updatedAt:     ZonedDateTime,
  statusesUrl:   String,
  repositoryUrl: String
)
