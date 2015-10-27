package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait LabelCodec {
  implicit lazy val labelDecoder = deriveFor[Label].decoder
}

case class Label(url: String, name: String, color: String)
