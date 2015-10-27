package tv.teads.github.api.model.webhook

import io.circe._

import tv.teads.github.api.model._

trait PushPayloadCodec {
  self: UserCodec with RepositoryCodec with CommitCodec with AuthorCodec ⇒

  implicit lazy val pushPayloadDecoder = Decoder.instance { cursor ⇒
    for {
      ref ← cursor.downField("ref").as[String]
      before ← cursor.downField("before").as[String]
      after ← cursor.downField("after").as[String]
      created ← cursor.downField("created").as[Boolean]
      deleted ← cursor.downField("deleted").as[Boolean]
      forced ← cursor.downField("forced").as[Boolean]
      baseRef ← cursor.downField("base_ref").as[Option[String]]
      compare ← cursor.downField("compare").as[String]
      commits ← cursor.downField("commits").as[List[Commit]]
      headCommit ← cursor.downField("head_commit").as[Option[Commit]]
      repository ← cursor.downField("repository").as[Repository]
      pusher ← cursor.downField("pusher").as[Author]
      sender ← cursor.downField("sender").as[User]
    } yield PushPayload(
      ref, before, after, created, deleted, forced, baseRef,
      compare, commits, headCommit, repository, pusher, sender
    )
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
