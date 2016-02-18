package tv.teads.github.api.model

import io.circe.generic.semiauto._

trait LabelCodec {
  implicit lazy val labelDecoder = deriveDecoder[Label]
}

case class Label(url: String, name: String, color: String)
