package tv.teads.github.api.model

import io.circe._

trait HookConfigCodec {
  implicit lazy val hookConfigDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[Option[String]]
      contentType ← cursor.downField("content_type").as[Option[String]]
    } yield HookConfig(url, contentType)
  }
}

case class HookConfig(url: Option[String], contentType: Option[String])
