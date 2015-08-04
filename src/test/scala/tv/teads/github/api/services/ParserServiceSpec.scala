package tv.teads.github.api.services

import play.api.libs.json.Json
import tv.teads.github.api.BaseSpec
import tv.teads.github.api.models.payloads._

class ParserServiceSpec extends BaseSpec {

  "ParserService" should "parse Pull Request payload" in {
    val lines = loadFile(s"files/pull_request.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "pull_request", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PullRequestPayload]
  }

  //  it should "parse Closed Pull Request payload" in {
  //    val lines = loadFile(s"files/pr_closed.json")
  //    val json = Json.parse(lines)
  //
  //    val js = Json.obj("event" -> "pull_request", "payload" -> json)
  //
  //    ParserService.parsePayload(js).fold(
  //       errs => errs.foreach(println),
  //      identity
  //    ) shouldBe a[PullRequestPayload]
  //  }

  it should "parse Issue payload" in {
    val lines = loadFile(s"files/issues.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "issues", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[IssuePayload]
  }

  it should "parse Push payload " in {
    val lines = loadFile(s"files/push.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "push", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PushPayload]
  }

  //  it should "parse Push on orga repo payload" in {
  //    val lines = loadFile(s"files/push_orga.json")
  //    val json = Json.parse(lines)
  //
  //    val js = Json.obj("event" -> "push", "payload" -> json)
  //
  //    ParserService.parsePayload(js).fold(
  //      errs => errs.foreach(println),
  //      identity
  //    ) shouldBe a[PushPayload]
  //  }

  //  it should "Fail when parsing Push event and Issue body " in {
  //    val lines = loadFile(s"files/issues.json")
  //    val json = Json.parse(lines)
  //
  //    //      route(FakeRequest(POST, "/hooks")
  //    //        .withHeaders("Content-Type" -> "application/json",
  //    //                    "X-Github-Event" -> "push")
  //    //        .withJsonBody(json)) must beSome
  //    val js = Json.obj("event" -> "push", "payload" -> json)
  //
  //    ParserService.parsePayload(js).fold(
  //       errs => errs.foreach(println),
  //      identity
  //    ) === "err"
  //  }

  it should "parse Issue comment payload" in {
    val lines = loadFile(s"files/issue_comment.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "issue_comment", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[IssueCommentPayload]
  }

  it should "parse Status payload" in {
    val lines = loadFile(s"files/status.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "status", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[StatusPayload]
  }

  it should "parse Create payload" in {
    val lines = loadFile(s"files/create.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "create", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[CreatePayload]
  }

  it should "parse Fork payload" in {
    val lines = loadFile(s"files/fork.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "fork", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[ForkPayload]
  }

  it should "parse Pull Request Comment Review payload" in {
    val lines = loadFile(s"files/pull_request_comment_review.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "pull_request_review_comment", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[PullRequestCommentReviewPayload]
  }

  it should "parse Repository payload" in {
    val lines = loadFile(s"files/repository.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "repository", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[RepositoryPayload]
  }

  it should "parse Team Add payload" in {
    val lines = loadFile(s"files/team_add.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "team_add", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[TeamAddPayload]
  }

  it should "parse Commit Comment payload" in {
    val lines = loadFile(s"files/commit_comment.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "commit_comment", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[CommitCommentPayload]
  }

  it should "parse Membership payload" in {
    val lines = loadFile(s"files/membership.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "membership", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[MembershipPayload]
  }

  it should "parse Delete payload" in {
    val lines = loadFile(s"files/delete.json")
    val json = Json.parse(lines)

    val js = Json.obj("event" → "delete", "payload" → json)

    ParserService.parsePayload(js).fold(
      errs ⇒ errs.foreach(println),
      identity
    ) shouldBe a[DeletePayload]
  }

}
