package tv.teads.github.api.model

import io.circe._, io.circe.syntax._

trait StatusCodec {
  implicit lazy val statusEncoder = Encoder.instance[Status] { status ⇒
    import status._
    Json.obj(
      "state" → state.asJson,
      "target_url" → targetUrl.asJson,
      "description" → description.asJson,
      "context" → context.asJson
    )
  }

  implicit lazy val statusDecoder = Decoder.instance { cursor ⇒
    for {
      state ← cursor.downField("state").as[StatusState]
      targetUrl ← cursor.downField("target_url").as[Option[String]]
      description ← cursor.downField("description").as[Option[String]]
      context ← cursor.downField("context").as[Option[String]]
    } yield Status(state, targetUrl, description, context)
  }
}

case class Status(
  state:       StatusState,
  targetUrl:   Option[String] = None,
  description: Option[String] = None,
  context:     Option[String] = None
)
