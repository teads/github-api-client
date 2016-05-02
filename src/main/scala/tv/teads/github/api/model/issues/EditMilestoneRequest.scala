package tv.teads.github.api.model.issues

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.DateTimeCodecs.ZoneDateTimeToIsoOffsetEncoder

object EditMilestoneRequest {
  implicit final val EditMilestoneRequestEncoder: Encoder[EditMilestoneRequest] =
    Encoder.forProduct4("title", "state", "description", "due_on")(req ⇒ EditMilestoneRequest.unapply(req).get)
}
case class EditMilestoneRequest(
  title:       Option[String],
  state:       Option[MilestoneState],
  description: Option[String],
  dueOn:       Option[ZonedDateTime]
)
