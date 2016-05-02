package tv.teads.github.api.model.repositories

import io.circe._
import tv.teads.github.api.model.common.Event

object EditWebhookRequest {

  implicit val editWebhookRequestEncoder: Encoder[EditWebhookRequest] = Encoder.forProduct5(
    "config", "events", "add_events", "remove_events", "active"
  )(req ⇒ EditWebhookRequest.unapply(req).get)
}
case class EditWebhookRequest(
  config:       Option[Map[String, String]] = None,
  events:       Option[List[Event]]         = None,
  addEvents:    Option[List[Event]]         = None,
  removeEvents: Option[List[Event]]         = None,
  active:       Option[Boolean]             = None
)