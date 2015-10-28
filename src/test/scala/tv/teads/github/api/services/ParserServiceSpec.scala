package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.model.webhook._

class ParserServiceSpec extends BaseSpec {

  "ParserService" should "parse Pull Request payload" in {
    val lines = loadFile(s"files/pull_request.json")
    ebuzzingClient.webhookPayloads.parsePayload("pull_request", lines).toOption.value shouldBe a[PullRequestPayload]
  }

  it should "parse Issue payload" in {
    val lines = loadFile(s"files/issues.json")
    ebuzzingClient.webhookPayloads.parsePayload("issues", lines).toOption.value shouldBe a[IssuePayload]
  }

  it should "parse Push payload " in {
    val lines = loadFile(s"files/push.json")
    ebuzzingClient.webhookPayloads.parsePayload("push", lines).toOption.value shouldBe a[PushPayload]
  }

  it should "parse Issue comment payload" in {
    val lines = loadFile(s"files/issue_comment.json")
    ebuzzingClient.webhookPayloads.parsePayload("issue_comment", lines).toOption.value shouldBe a[IssueCommentPayload]
  }

  it should "parse Status payload" in {
    val lines = loadFile(s"files/status.json")
    ebuzzingClient.webhookPayloads.parsePayload("status", lines).toOption.value shouldBe a[StatusPayload]
  }

  it should "parse Create payload" in {
    val lines = loadFile(s"files/create.json")
    ebuzzingClient.webhookPayloads.parsePayload("create", lines).toOption.value shouldBe a[CreatePayload]
  }

  it should "parse Fork payload" in {
    val lines = loadFile(s"files/fork.json")
    ebuzzingClient.webhookPayloads.parsePayload("fork", lines).toOption.value shouldBe a[ForkPayload]
  }

  it should "parse Pull Request Comment Review payload" in {
    val lines = loadFile(s"files/pull_request_comment_review.json")
    ebuzzingClient.webhookPayloads.parsePayload("pull_request_review_comment", lines).toOption.value shouldBe a[PullRequestCommentReviewPayload]
  }

  it should "parse Repository payload" in {
    val lines = loadFile(s"files/repository.json")
    ebuzzingClient.webhookPayloads.parsePayload("repository", lines).toOption.value shouldBe a[RepositoryPayload]
  }

  it should "parse Team Add payload" in {
    val lines = loadFile(s"files/team_add.json")
    ebuzzingClient.webhookPayloads.parsePayload("team_add", lines).toOption.value shouldBe a[TeamAddPayload]
  }

  it should "parse Commit Comment payload" in {
    val lines = loadFile(s"files/commit_comment.json")
    ebuzzingClient.webhookPayloads.parsePayload("commit_comment", lines).toOption.value shouldBe a[CommitCommentPayload]
  }

  it should "parse Membership payload" in {
    val lines = loadFile(s"files/membership.json")
    ebuzzingClient.webhookPayloads.parsePayload("membership", lines).toOption.value shouldBe a[MembershipPayload]
  }

  it should "parse Delete payload" in {
    val lines = loadFile(s"files/delete.json")
    ebuzzingClient.webhookPayloads.parsePayload("delete", lines).toOption.value shouldBe a[DeletePayload]
  }

}
