package tv.teads.github.api.model.repositories

import java.time.ZonedDateTime
import io.circe._, io.circe.generic.auto._
import tv.teads.github.api.model.common.Event
import tv.teads.github.api.model.DateTimeCodecs.IsoOffsetToZoneDateTimeDecoder

case class WebhookLastResponse(code: Int, status: String, message: String)

object RepositoryWebhook {
  implicit val repositoryWebhookDecoder = Decoder.forProduct11(
    "id", "name", "active", "events", "config", "updated_at",
    "created_at", "url", "test_url", "ping_url", "last_response"
  )(RepositoryWebhook.apply)
}
case class RepositoryWebhook(
  id:           Int,
  name:         String,
  active:       Boolean,
  events:       List[Event],
  config:       Map[String, String],
  updatedAt:    ZonedDateTime,
  createdAt:    ZonedDateTime,
  url:          String,
  testUrl:      String,
  pingUrl:      String,
  lastResponse: Option[WebhookLastResponse]
)