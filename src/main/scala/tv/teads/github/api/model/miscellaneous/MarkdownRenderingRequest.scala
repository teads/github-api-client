package tv.teads.github.api.model.miscellaneous

import enumeratum._
import io.circe.generic.semiauto._

sealed abstract class MarkdownRenderMode(name: String) extends EnumEntry {
  override def entryName = name
}

object MarkdownRenderMode extends CirceEnum[MarkdownRenderMode] with Enum[MarkdownRenderMode] {
  override val values: Seq[MarkdownRenderMode] = findValues

  case object PlainMarkdown extends MarkdownRenderMode("markdown")
  case object GithubFlavoredMarkdown extends MarkdownRenderMode("gfm")
}

trait MarkdownRenderingRequestCodec {
  implicit val markdownRenderingRequestEncoder = deriveEncoder[MarkdownRenderingRequest]
}
case class MarkdownRenderingRequest(
  text:    String,
  context: String,
  mode:    MarkdownRenderMode = MarkdownRenderMode.PlainMarkdown
)
