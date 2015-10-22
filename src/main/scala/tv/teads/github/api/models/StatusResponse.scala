package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.libs.json.JsValue
import play.api.data.mapping._
import play.api.libs.functional.syntax._

trait StatusResponseFormats {
  self: StatusFormats with UserFormats ⇒

  implicit lazy val statusResponseJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      statusJsonRead ~
      (__ \ "created_at").read(zonedDateTime) ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "id").read[Long] ~
      (__ \ "url").read[String] ~
      (__ \ "creator").read[User]

    )(StatusResponse.apply _)

  }
}

case class StatusResponse(
  status:    Status,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  id:        Long,
  url:       String,
  creator:   User
)
