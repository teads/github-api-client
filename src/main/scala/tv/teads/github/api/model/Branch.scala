package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait BranchCodec {
  self: UserCodec with TreeCodec ⇒

  implicit lazy val branchDecoder = deriveDecoder[Branch]
}
case class Branch(name: String, commit: Tree)
