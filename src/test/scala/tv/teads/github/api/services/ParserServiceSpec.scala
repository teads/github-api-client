package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec
import tv.teads.github.api.models.payloads._

class ParserServiceSpec extends BaseSpec {

  "ParserService" should "parse Pull Request payload" in {
    val lines = loadFile(s"files/pull_request.json")

    ParserService.parsePayload("pull_request", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PullRequestPayload]
  }

  it should "parse Issue payload" in {
    val lines = loadFile(s"files/issues.json")

    ParserService.parsePayload("issues", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[IssuePayload]
  }

  it should "parse Push payload " in {
    val lines = loadFile(s"files/push.json")

    ParserService.parsePayload("push", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PushPayload]
  }

  it should "parse Issue comment payload" in {
    val lines = loadFile(s"files/issue_comment.json")

    ParserService.parsePayload("issue_comment", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[IssueCommentPayload]
  }

  it should "parse Status payload" in {
    val lines = loadFile(s"files/status.json")

    ParserService.parsePayload("status", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[StatusPayload]
  }

  it should "parse Create payload" in {
    val lines = loadFile(s"files/create.json")

    ParserService.parsePayload("create", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[CreatePayload]
  }

  it should "parse Fork payload" in {
    val lines = loadFile(s"files/fork.json")

    ParserService.parsePayload("fork", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[ForkPayload]
  }

  it should "parse Pull Request Comment Review payload" in {
    val lines = loadFile(s"files/pull_request_comment_review.json")

    ParserService.parsePayload("pull_request_review_comment", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PullRequestCommentReviewPayload]
  }

  it should "parse Repository payload" in {
    val lines = loadFile(s"files/repository.json")

    ParserService.parsePayload("repository", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[RepositoryPayload]
  }

  it should "parse Team Add payload" in {
    val lines = loadFile(s"files/team_add.json")

    ParserService.parsePayload("team_add", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[TeamAddPayload]
  }

  it should "parse Commit Comment payload" in {
    val lines = loadFile(s"files/commit_comment.json")

    ParserService.parsePayload("commit_comment", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[CommitCommentPayload]
  }

  it should "parse Membership payload" in {
    val lines = loadFile(s"files/membership.json")

    ParserService.parsePayload("membership", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[MembershipPayload]
  }

  it should "parse Delete payload" in {
    val lines = loadFile(s"files/delete.json")

    ParserService.parsePayload("delete", lines).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[DeletePayload]
  }

}
