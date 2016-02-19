package tv.teads.github.api

import tv.teads.github.api.model.webhook._

class WebhookPayloadParserSpec extends BaseSpec {

  "ParserService" should "parse Pull Request payload" in {
    val lines = loadFile(s"files/pull_request.json")
    WebhookPayloadParser.parsePayload("pull_request", lines).toOption.value shouldBe a[PullRequestPayload]
  }

  it should "parse Issue payload" in {
    val lines = loadFile(s"files/issues.json")
    WebhookPayloadParser.parsePayload("issues", lines).toOption.value shouldBe a[IssuePayload]
  }

  it should "parse Push payload " in {
    val lines = loadFile(s"files/push.json")
    WebhookPayloadParser.parsePayload("push", lines).toOption.value shouldBe a[PushPayload]
  }

  it should "parse Issue comment payload" in {
    val lines = loadFile(s"files/issue_comment.json")
    WebhookPayloadParser.parsePayload("issue_comment", lines).toOption.value shouldBe a[IssueCommentPayload]
  }

  it should "parse Status payload" in {
    val lines = loadFile(s"files/status.json")
    WebhookPayloadParser.parsePayload("status", lines).toOption.value shouldBe a[StatusPayload]
  }

  it should "parse Create payload" in {
    val lines = loadFile(s"files/create.json")
    WebhookPayloadParser.parsePayload("create", lines).toOption.value shouldBe a[CreatePayload]
  }

  it should "parse Fork payload" in {
    val lines = loadFile(s"files/fork.json")
    WebhookPayloadParser.parsePayload("fork", lines).toOption.value shouldBe a[ForkPayload]
  }

  it should "parse Pull Request Comment Review payload" in {
    val lines = loadFile(s"files/pull_request_comment_review.json")
    WebhookPayloadParser.parsePayload("pull_request_review_comment", lines).toOption.value shouldBe a[PullRequestCommentReviewPayload]
  }

  it should "parse Repository payload" in {
    val lines = loadFile(s"files/repository.json")
    WebhookPayloadParser.parsePayload("repository", lines).toOption.value shouldBe a[RepositoryPayload]
  }

  it should "parse Team Add payload" in {
    val lines = loadFile(s"files/team_add.json")
    WebhookPayloadParser.parsePayload("team_add", lines).toOption.value shouldBe a[TeamAddPayload]
  }

  it should "parse Commit Comment payload" in {
    val lines = loadFile(s"files/commit_comment.json")
    WebhookPayloadParser.parsePayload("commit_comment", lines).toOption.value shouldBe a[CommitCommentPayload]
  }

  it should "parse Membership payload" in {
    val lines = loadFile(s"files/membership.json")
    WebhookPayloadParser.parsePayload("membership", lines).toOption.value shouldBe a[MembershipPayload]
  }

  it should "parse Delete payload" in {
    val lines = loadFile(s"files/delete.json")
    WebhookPayloadParser.parsePayload("delete", lines).toOption.value shouldBe a[DeletePayload]
  }

  it should "parse Release payload" in {
    val lines = loadFile(s"files/release.json")
    WebhookPayloadParser.parsePayload("release", lines).toOption.value shouldBe a[ReleasePayload]
  }

  it should "parse Deployment payload" in {
    val lines = loadFile(s"files/deployment.json")
    WebhookPayloadParser.parsePayload("deployment", lines).toOption.value shouldBe a[DeploymentPayload]
  }

  it should "parse Deployment Status payload" in {
    val lines = loadFile(s"files/deployment_status.json")
    WebhookPayloadParser.parsePayload("deployment_status", lines).toOption.value shouldBe a[DeploymentStatusPayload]
  }

}
