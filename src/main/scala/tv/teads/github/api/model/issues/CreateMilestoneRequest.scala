package tv.teads.github.api.model.issues

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.model.DateTimeCodecs.ZoneDateTimeToIsoOffsetEncoder

object CreateMilestoneRequest {
  implicit final val CreateMilestoneRequestEncoder: Encoder[CreateMilestoneRequest] =
    Encoder.forProduct4("title", "state", "description", "due_on")(req ⇒ CreateMilestoneRequest.unapply(req).get)
}
case class CreateMilestoneRequest(
  title:       String,
  state:       Option[MilestoneState],
  description: Option[String],
  dueOn:       Option[ZonedDateTime]
)
