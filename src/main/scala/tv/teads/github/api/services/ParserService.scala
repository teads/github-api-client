package tv.teads.github.api.services

import play.api.data.mapping._
import play.api.libs.json.JsValue
import tv.teads.github.api.models.payloads._
import tv.teads.github.api.models.payloads.Events._

object ParserService extends PayloadFormats {

  private val typeFailure = Failure(Seq(Path → Seq(ValidationError("validation.unknownEvent"))))

  def parsePayload(payload: JsValue) = {

    val rule: Rule[JsValue, Payload] = From[JsValue] { __ ⇒
      import play.api.data.mapping.json.Rules._

      val payload: Reader[JsValue] = __ \ "payload"
      val event: Reader[JsValue] = __ \ "event"

      event.read[Event].flatMap[Payload] {
        case Event.issues                      ⇒ payload.read[IssuePayload].fmap(x ⇒ x)
        case Event.pull_request                ⇒ payload.read[PullRequestPayload].fmap(x ⇒ x)
        case Event.push                        ⇒ payload.read[PushPayload].fmap(x ⇒ x)
        case Event.issue_comment               ⇒ payload.read[IssueCommentPayload].fmap(x ⇒ x)
        case Event.status                      ⇒ payload.read[StatusPayload].fmap(x ⇒ x)
        case Event.create                      ⇒ payload.read[CreatePayload].fmap(x ⇒ x)
        case Event.fork                        ⇒ payload.read[ForkPayload].fmap(x ⇒ x)
        case Event.pull_request_review_comment ⇒ payload.read[PullRequestCommentReviewPayload].fmap(x ⇒ x)
        case Event.repository                  ⇒ payload.read[RepositoryPayload].fmap(x ⇒ x)
        case Event.team_add                    ⇒ payload.read[TeamAddPayload].fmap(x ⇒ x)
        case Event.commit_comment              ⇒ payload.read[CommitCommentPayload].fmap(x ⇒ x)
        case Event.membership                  ⇒ payload.read[MembershipPayload].fmap(x ⇒ x)
        case Event.delete                      ⇒ payload.read[DeletePayload].fmap(x ⇒ x)
        case _                                 ⇒ Rule(_ ⇒ typeFailure)
      }
    }

    rule.validate(payload)
  }

}
