package tv.teads.github.api.model.webhook

import io.circe._

import tv.teads.github.api.model._

trait PullRequestCommentReviewPayloadCodec {
  self: UserCodec with RepositoryCodec with PullRequestCodec with PullRequestCommentCodec ⇒

  implicit lazy val pullRequestCommentReviewPayloadDecoder = Decoder.instance { cursor ⇒
    for {
      action ← cursor.downField("action").as[PullRequestReviewCommentAction]
      comment ← cursor.downField("comment").as[PullRequestReviewComment]
      pullRequest ← cursor.downField("pull_request").as[PullRequest]
      repository ← cursor.downField("repository").as[Repository]
      organization ← cursor.downField("organization").as[Option[User]]
      sender ← cursor.downField("sender").as[User]
    } yield PullRequestCommentReviewPayload(action, comment, pullRequest, repository, organization, sender)
  }
}

case class PullRequestCommentReviewPayload(
  action:       PullRequestReviewCommentAction,
  comment:      PullRequestReviewComment,
  pullRequest:  PullRequest,
  repository:   Repository,
  organization: Option[User],
  sender:       User
) extends Payload
