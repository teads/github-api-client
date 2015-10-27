package tv.teads.github.api.model.webhook

import io.circe._

import tv.teads.github.api.model._

trait PullRequestPayloadCodec {
  self: UserCodec with RepositoryCodec with PullRequestCodec ⇒

  implicit lazy val pullRequestPayloadDecoder = Decoder.instance { cursor ⇒
    for {
      action ← cursor.downField("action").as[PullRequestAction]
      number ← cursor.downField("number").as[Long]
      pullRequest ← cursor.downField("pull_request").as[PullRequest]
      repository ← cursor.downField("repository").as[Repository]
      sender ← cursor.downField("sender").as[User]
    } yield PullRequestPayload(action, number, pullRequest, repository, sender)
  }
}
case class PullRequestPayload(
  action:      PullRequestAction,
  number:      Long,
  pullRequest: PullRequest,
  repository:  Repository,
  sender:      User
) extends Payload
