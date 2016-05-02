package tv.teads.github.api.model.repositories

import tv.teads.github.api.model.common.Event

case class CreateWebhookRequest(
  name:   String,
  config: Map[String, String],
  events: Option[List[Event]] = None,
  active: Boolean
)
