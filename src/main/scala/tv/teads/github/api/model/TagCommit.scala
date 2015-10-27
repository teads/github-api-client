package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait TagCommitCodec {
  implicit lazy val tagCommitDecoder = deriveFor[TagCommit].decoder
}
case class TagCommit(sha: String, url: String)
