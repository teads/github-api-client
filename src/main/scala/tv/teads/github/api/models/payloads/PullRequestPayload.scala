package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models.actions.PullRequestActions
import PullRequestActions.PullRequestAction
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PullRequestPayloadFormats {
  self: UserFormats with RepositoryFormats with PullRequestFormats ⇒

  implicit lazy val pullRequestPayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "action").read[PullRequestAction] ~
      (__ \ "number").read[Long] ~
      (__ \ "pull_request").read[PullRequest] ~
      (__ \ "repository").read[Repository] ~
      (__ \ "sender").read[User]
    )(PullRequestPayload.apply _)
  }

}
case class PullRequestPayload(
  action:      PullRequestAction,
  number:      Long,
  pullRequest: PullRequest,
  repository:  Repository,
  sender:      User
) extends Payload
