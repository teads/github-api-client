package tv.teads.github.api.model.webhook

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

import tv.teads.github.api.model._

trait StatusPayloadCodec {
  self: UserCodec with RepositoryCodec with GHCommitCodec with BranchCodec with ZonedDateTimeCodec ⇒

  implicit lazy val statusPayloadDecoder = Decoder.instance { cursor ⇒
    for {
      id ← cursor.downField("id").as[Long]
      sha ← cursor.downField("sha").as[String]
      name ← cursor.downField("name").as[String]
      targetUrl ← cursor.downField("target_url").as[Option[String]]
      context ← cursor.downField("context").as[String]
      description ← cursor.downField("description").as[Option[String]]
      state ← cursor.downField("state").as[StatusState]
      commit ← cursor.downField("commit").as[GHCommit]
      branches ← cursor.downField("branches").as[List[Branch]]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      repository ← cursor.downField("repository").as[Repository]
      sender ← cursor.downField("sender").as[User]
    } yield StatusPayload(
      id, sha, name, targetUrl, context, description, state, commit,
      branches, createdAt, updatedAt, repository, sender
    )
  }

}
case class StatusPayload(
  id:          Long,
  sha:         String,
  name:        String,
  targetUrl:   Option[String],
  context:     String,
  description: Option[String],
  state:       StatusState,
  commit:      GHCommit,
  branches:    List[Branch],
  createdAt:   ZonedDateTime,
  updatedAt:   ZonedDateTime,
  repository:  Repository,
  sender:      User
) extends Payload
