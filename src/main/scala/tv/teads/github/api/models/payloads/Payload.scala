package tv.teads.github.api.models.payloads

import tv.teads.github.api.models._
import tv.teads.github.api.models.common.ADTEnum

trait PayloadFormats extends RepositoryFormats
  with UserFormats
  with TagFormats
  with TagCommitFormats
  with HeadFormats
  with TreeFormats
  with RepositoryUrlsFormats
  with PermissionFormats
  with RepositoryConfigFormats
  with RepositoryStatsFormats
  with ParentFormats
  with CommitDetailFormats
  with GHCommitFormats
  with AuthorFormats
  with TeamFormats
  with LabelFormats
  with CommentFormats
  with IssueFormats
  with CommitCommentFormats
  with CommitFormats
  with PullRequestUrlsFormats
  with HRefFormats
  with LinksFormats
  with TimeMetadataFormats
  with ChangeMetadataFormats
  with PullRequestFormats
  with ReleaseFormats
  with BranchFormats
  with CoreFormats
  with ResourcesFormats
  with RateLimitFormats
  with PullRequestReviewCommentLinksFormats
  with PullRequestCommentFormats
  with FileFormats
  with StatusFormats
  with StatusResponseFormats
  with RepositoryPayloadFormats
  with DeletePayloadFormats
  with CommitCommentPayloadFormats
  with CreatePayloadFormats
  with DeploymentPayloadFormats
  with DeploymentStatusPayloadFormats
  with ForkPayloadFormats
  with GollumPayloadFormats
  with IssueCommentPayloadFormats
  with IssuePayloadFormats
  with MemberPayloadFormats
  with MembershipPayloadFormats
  with PageBuildPayloadFormats
  with PublicPayloadFormats
  with PullRequestCommentReviewPayloadFormats
  with PullRequestPayloadFormats
  with PushPayloadFormats
  with ReleasePayloadFormats
  with StatusPayloadFormats
  with TeamAddPayloadFormats
  with WatchPayloadFormats
  with OrganizationFormats

trait Payload

object Events {

  sealed trait Event
  // https://developer.github.com/webhooks/#events

  object Event extends ADTEnum[Event] {
    case object commit_comment extends Event
    case object create extends Event
    case object delete extends Event
    case object deployment extends Event
    case object deployment_status extends Event
    case object fork extends Event
    case object gollum extends Event
    case object issue_comment extends Event
    case object issues extends Event
    case object member extends Event
    case object membership extends Event
    case object public extends Event
    case object pull_request_review_comment extends Event
    case object pull_request extends Event
    case object push extends Event
    case object repository extends Event
    case object release extends Event
    case object status extends Event
    case object team_add extends Event
    case object watch extends Event
    val list = Seq(
      commit_comment,
      create,
      delete,
      deployment,
      deployment_status,
      fork,
      gollum,
      issue_comment,
      issues,
      member,
      membership,
      public,
      pull_request_review_comment,
      pull_request,
      push,
      repository,
      release,
      status,
      team_add,
      watch
    )
  }
}
