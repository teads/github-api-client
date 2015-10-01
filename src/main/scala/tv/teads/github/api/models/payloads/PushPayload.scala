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

  implicit lazy val pushPayloadJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "ref").read[String] ~
      (__ \ "before").read[String] ~
      (__ \ "after").read[String] ~
      (__ \ "created").read[Boolean] ~
      (__ \ "deleted").read[Boolean] ~
      (__ \ "forced").read[Boolean] ~
      (__ \ "base_ref").read[Option[String]] ~
      (__ \ "compare").read[String] ~
      (__ \ "commits").read[List[Commit]] ~
      (__ \ "head_commits").read[Option[Commit]] ~
      (__ \ "repository").read[Repository] ~
      (__ \ "pusher").read[Author] ~
      (__ \ "sender").read[User]
    )(PushPayload.apply _)
  }

}
case class PushPayload(
  ref:        String,
  before:     String,
  after:      String,
  created:    Boolean,
  deleted:    Boolean,
  forced:     Boolean,
  baseRef:    Option[String],
  compare:    String,
  commits:    List[Commit],
  headCommit: Option[Commit],
  repository: Repository,
  pusher:     Author,
  sender:     User
) extends Payload
