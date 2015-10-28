package tv.teads.github.api.services

import cats.data.Xor
import io.circe._, io.circe.jawn._
import tv.teads.github.api.model.webhook._

class ParserService extends WebhooksCodecs {

  def parsePayload(event: String, payload: String): Xor[Error, Payload] =
    parse(payload).flatMap[Error, Payload] { json ⇒
      Event.values.find(_.toString == event).map {
        case Event.issues                      ⇒ json.as[IssuePayload]
        case Event.pull_request                ⇒ json.as[PullRequestPayload]
        case Event.push                        ⇒ json.as[PushPayload]
        case Event.issue_comment               ⇒ json.as[IssueCommentPayload]
        case Event.status                      ⇒ json.as[StatusPayload]
        case Event.create                      ⇒ json.as[CreatePayload]
        case Event.fork                        ⇒ json.as[ForkPayload]
        case Event.pull_request_review_comment ⇒ json.as[PullRequestCommentReviewPayload]
        case Event.repository                  ⇒ json.as[RepositoryPayload]
        case Event.team_add                    ⇒ json.as[TeamAddPayload]
        case Event.commit_comment              ⇒ json.as[CommitCommentPayload]
        case Event.membership                  ⇒ json.as[MembershipPayload]
        case Event.delete                      ⇒ json.as[DeletePayload]
        case Event.public                      ⇒ json.as[PublicPayload]
        case Event.watch                       ⇒ json.as[WatchPayload]
        case Event.deployment                  ⇒ json.as[DeploymentPayload]
        case Event.deployment_status           ⇒ json.as[DeploymentStatusPayload]
        case Event.gollum                      ⇒ json.as[GollumPayload]
        case Event.member                      ⇒ json.as[MemberPayload]
        case Event.release                     ⇒ json.as[ReleasePayload]
      }.getOrElse(Xor.left(DecodingFailure("webhook.unknownEvent", json.hcursor.history)))
    }
}
