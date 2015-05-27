package tv.teads.github.api.models.payloads

import play.api.libs.json.{JsObject, JsValue}
import tv.teads.github.api.models.actions.PullRequestActions
import PullRequestActions.PullRequestAction
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PullRequestPayloadFormats {
  self: UserFormats with RepositoryFormats  with PullRequestFormats =>

  implicit lazy val  pullRequestPayloadJsonWrite : Write[PullRequestPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestPayload, JsObject]
  }

  implicit lazy val  pullRequestPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, PullRequestPayload]
  }

}
case class PullRequestPayload(
                        action: PullRequestAction,
                        number: Long,
                        pull_request: PullRequest,
                        repository: Repository,
                        sender: User
                        ) extends Payload
