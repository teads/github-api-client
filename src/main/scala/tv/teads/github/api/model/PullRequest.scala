package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait PullRequestUrlsCodec {
  implicit lazy val pullRequestUrlsDecoder = Decoder.instance { cursor ⇒
    for {
      htmlUrl ← cursor.downField("html_url").as[String]
      diffUrl ← cursor.downField("diff_url").as[String]
      patchUrl ← cursor.downField("patch_url").as[String]
      issueUrl ← cursor.downField("issue_url").as[String]
      commitsUrl ← cursor.downField("commits_url").as[String]
      reviewCommentsUrl ← cursor.downField("review_comments_url").as[String]
      reviewCommentUrl ← cursor.downField("review_comment_url").as[String]
      commentsUrl ← cursor.downField("comments_url").as[String]
      statusesUrl ← cursor.downField("statuses_url").as[String]
    } yield PullRequestUrls(
      htmlUrl, diffUrl, patchUrl, issueUrl, commitsUrl, reviewCommentsUrl,
      reviewCommentUrl, commentsUrl, statusesUrl
    )
  }

}
case class PullRequestUrls(
  htmlUrl:           String,
  diffUrl:           String,
  patchUrl:          String,
  issueUrl:          String,
  commitsUrl:        String,
  reviewCommentsUrl: String,
  reviewCommentUrl:  String,
  commentsUrl:       String,
  statusesUrl:       String
)

trait TimeMetadataCodec {
  self: ZonedDateTimeCodec ⇒

  implicit lazy val timeMetadataDecoder = Decoder.instance { cursor ⇒
    for {
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      closedAt ← cursor.downField("closed_at").as[Option[ZonedDateTime]]
      mergedAt ← cursor.downField("merged_at").as[Option[ZonedDateTime]]
      mergeCommitSha ← cursor.downField("merge_commit_sha").as[Option[String]]
    } yield TimeMetadata(createdAt, updatedAt, closedAt, mergedAt, mergeCommitSha)
  }
}
case class TimeMetadata(
  createdAt:      ZonedDateTime,
  updatedAt:      ZonedDateTime,
  closedAt:       Option[ZonedDateTime],
  mergedAt:       Option[ZonedDateTime],
  mergeCommitSha: Option[String]
)

trait ChangeMetadataCodec {
  implicit lazy val changeMetadataDecoder = Decoder.instance { cursor ⇒
    for {
      comments ← cursor.downField("comments").as[Option[Long]]
      reviewComments ← cursor.downField("review_comments").as[Option[Long]]
      commits ← cursor.downField("commits").as[Option[Long]]
      additions ← cursor.downField("additions").as[Option[Long]]
      deletions ← cursor.downField("deletions").as[Option[Long]]
      changedFiles ← cursor.downField("changed_files").as[Option[Long]]
    } yield ChangeMetadata(comments, reviewComments, commits, additions, deletions, changedFiles)
  }
}
case class ChangeMetadata(
  comments:       Option[Long],
  reviewComments: Option[Long],
  commits:        Option[Long],
  additions:      Option[Long],
  deletions:      Option[Long],
  changedFiles:   Option[Long]
)

trait PullRequestCodec {
  self: UserCodec with PullRequestUrlsCodec with LinksCodec with TimeMetadataCodec with ChangeMetadataCodec with HeadCodec with MilestoneCodec ⇒

  implicit lazy val pullRequestDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      id ← cursor.downField("id").as[Long]
      number ← cursor.downField("number").as[Long]
      state ← cursor.downField("state").as[String]
      locked ← cursor.downField("locked").as[Boolean]
      title ← cursor.downField("title").as[String]
      user ← cursor.downField("user").as[User]
      body ← cursor.downField("body").as[String]
      assignee ← cursor.downField("assignee").as[Option[User]]
      milestone ← cursor.downField("milestone").as[Option[Milestone]]
      head ← cursor.downField("head").as[Head]
      base ← cursor.downField("base").as[Head]
      merged ← cursor.downField("merged").as[Option[Boolean]]
      mergeable ← cursor.downField("mergeable").as[Option[Boolean]]
      mergeableState ← cursor.downField("mergeable_state").as[Option[String]]
      mergedBy ← cursor.downField("merged_by").as[Option[User]]
      links ← cursor.downField("_links").as[Links]
      pullRequestUrls ← cursor.as[PullRequestUrls]
      timeMetadata ← cursor.as[TimeMetadata]
      changeMetadata ← cursor.as[ChangeMetadata]
    } yield PullRequest(
      url, id, number, state, locked, title, user, body, assignee, milestone, head, base, merged,
      mergeable, mergeableState, mergedBy, links, pullRequestUrls, timeMetadata, changeMetadata
    )
  }

}
case class PullRequest(
    url:            String,
    id:             Long,
    number:         Long,
    state:          String,
    locked:         Boolean,
    title:          String,
    user:           User,
    body:           String,
    assignee:       Option[User],
    milestone:      Option[Milestone],
    head:           Head,
    base:           Head,
    merged:         Option[Boolean],
    mergeable:      Option[Boolean],
    mergeableState: Option[String],
    mergedBy:       Option[User],
    links:          Links,
    urls:           PullRequestUrls,
    timeMetadata:   TimeMetadata,
    changeMetadata: ChangeMetadata
) {
  def status: PullRequestStatus =
    if (state == "open") PullRequestStatus.open
    else if (state == "closed" && timeMetadata.mergedAt.isDefined) PullRequestStatus.merged
    else PullRequestStatus.closed
}
