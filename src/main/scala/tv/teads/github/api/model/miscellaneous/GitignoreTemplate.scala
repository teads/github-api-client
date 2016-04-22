package tv.teads.github.api.model.miscellaneous

import io.circe.generic.semiauto._

trait GitignoreTemplateCodec {
  implicit val gitignoreTemplateDecoder = deriveDecoder[GitignoreTemplate]
}
case class GitignoreTemplate(name: String, source: String)
