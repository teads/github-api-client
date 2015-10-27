package tv.teads.github.api.model.webhook

import tv.teads.github.api.model.GithubApiCodecs

trait WebhooksCodecs extends GithubApiCodecs
  with RepositoryPayloadCodec
  with DeletePayloadCodec
  with CommitCommentPayloadCodec
  with CreatePayloadCodec
  with DeploymentPayloadCodec
  with DeploymentStatusPayloadCodec
  with ForkPayloadCodec
  with GollumPayloadCodec
  with IssueCommentPayloadCodec
  with IssuePayloadCodec
  with MemberPayloadCodec
  with MembershipPayloadCodec
  with PageBuildPayloadCodec
  with PublicPayloadCodec
  with PullRequestCommentReviewPayloadCodec
  with PullRequestPayloadCodec
  with PushPayloadCodec
  with ReleasePayloadCodec
  with StatusPayloadCodec
  with TeamAddPayloadCodec
  with WatchPayloadCodec

object WebhooksCodecs extends WebhooksCodecs