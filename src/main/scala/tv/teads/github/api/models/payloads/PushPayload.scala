package tv.teads.github.api.models.payloads

import play.api.libs.json.{ JsObject, JsValue }
import tv.teads.github.api.models._
import play.api.data.mapping._

trait PushPayloadFormats {
  self: UserFormats with RepositoryFormats with CommitFormats with AuthorFormats ⇒

  implicit lazy val pushPayloadJsonWrite: Write[PushPayload, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PushPayload, JsObject]
  }

  implicit lazy val pushPayloadJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, PushPayload]
  }

}
case class PushPayload(
  ref:         String,
  before:      String,
  after:       String,
  created:     Boolean,
  deleted:     Boolean,
  forced:      Boolean,
  base_ref:    Option[String],
  compare:     String,
  commits:     List[Commit],
  head_commit: Option[Commit],
  repository:  Repository,
  pusher:      Author,
  sender:      User
) extends Payload
