package tv.teads.github.api.services

import play.api.data.mapping._
import play.api.libs.json.{Json, JsValue}
import tv.teads.github.api.models.payloads._
import tv.teads.github.api.models.payloads.Events._

object ParserService extends PayloadFormats {

  private val typeFailure = Failure(Seq(Path → Seq(ValidationError("validation.unknownEvent"))))

  def parsePayload(event: String, payload: String) = {

    val body = Json.parse(payload)
    val json = Json.obj("event" → event, "payload" → body)

    val rule: Rule[JsValue, Payload] = From[JsValue] { __ ⇒
      import play.api.data.mapping.json.Rules._

      val payloadJs: Reader[JsValue] = __ \ "payload"
      val eventJs: Reader[JsValue] = __ \ "event"

      eventJs.read[Event].flatMap[Payload] {
        case Event.issues                      ⇒ payloadJs.read[IssuePayload].fmap(x ⇒ x)
        case Event.pull_request                ⇒ payloadJs.read[PullRequestPayload].fmap(x ⇒ x)
        case Event.push                        ⇒ payloadJs.read[PushPayload].fmap(x ⇒ x)
        case Event.issue_comment               ⇒ payloadJs.read[IssueCommentPayload].fmap(x ⇒ x)
        case Event.status                      ⇒ payloadJs.read[StatusPayload].fmap(x ⇒ x)
        case Event.create                      ⇒ payloadJs.read[CreatePayload].fmap(x ⇒ x)
        case Event.fork                        ⇒ payloadJs.read[ForkPayload].fmap(x ⇒ x)
        case Event.pull_request_review_comment ⇒ payloadJs.read[PullRequestCommentReviewPayload].fmap(x ⇒ x)
        case Event.repository                  ⇒ payloadJs.read[RepositoryPayload].fmap(x ⇒ x)
        case Event.team_add                    ⇒ payloadJs.read[TeamAddPayload].fmap(x ⇒ x)
        case Event.commit_comment              ⇒ payloadJs.read[CommitCommentPayload].fmap(x ⇒ x)
        case Event.membership                  ⇒ payloadJs.read[MembershipPayload].fmap(x ⇒ x)
        case Event.delete                      ⇒ payloadJs.read[DeletePayload].fmap(x ⇒ x)
        case _                                 ⇒ Rule(_ ⇒ typeFailure)
      }
    }

    rule.validate(json)
  }

}
