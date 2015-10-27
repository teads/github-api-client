package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._

import tv.teads.github.api.json.ZonedDateTimeCodec

trait PullRequestCommentCodec {
  self: UserCodec with PullRequestReviewCommentLinksCodec with ZonedDateTimeCodec ⇒

  implicit lazy val pullRequestCommentDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      id ← cursor.downField("id").as[Long]
      diffHunk ← cursor.downField("diff_hunk").as[String]
      path ← cursor.downField("path").as[String]
      position ← cursor.downField("position").as[Option[Long]]
      originalPosition ← cursor.downField("original_position").as[Option[Long]]
      commitId ← cursor.downField("commit_id").as[String]
      originalCommitId ← cursor.downField("original_commit_id").as[String]
      user ← cursor.downField("user").as[User]
      body ← cursor.downField("body").as[String]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      htmlUrl ← cursor.downField("html_url").as[String]
      pullRequestUrl ← cursor.downField("pull_request_url").as[String]
      links ← cursor.downField("_links").as[PullRequestReviewCommentLinks]
    } yield PullRequestReviewComment(
      url, id, diffHunk, path, position, originalPosition, commitId, originalCommitId,
      user, body, createdAt, updatedAt, htmlUrl, pullRequestUrl, links
    )
  }

}

case class PullRequestReviewComment(
  url:              String,
  id:               Long,
  diffHunk:         String,
  path:             String,
  position:         Option[Long],
  originalPosition: Option[Long],
  commitId:         String,
  originalCommitId: String,
  user:             User,
  body:             String,
  createdAt:        ZonedDateTime,
  updatedAt:        ZonedDateTime,
  htmlUrl:          String,
  pullRequestUrl:   String,
  links:            PullRequestReviewCommentLinks
)
