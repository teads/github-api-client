package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait CommitCodec {
  self: UserCodec with AuthorCodec ⇒

  implicit lazy val commitDecoder = deriveDecoder[Commit]
}

case class Commit(
  id:        String,
  distinct:  Boolean,
  message:   String,
  timestamp: String,
  url:       String,
  author:    Author,
  committer: Author,
  added:     List[String],
  removed:   List[String],
  modified:  List[String]
)
