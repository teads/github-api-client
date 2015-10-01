package tv.teads.github.api.models

import play.api.libs.json.JsValue
import play.api.libs.json._
import play.api.data.mapping._
import play.api.libs.functional.syntax._
import tv.teads.github.api.models.StatusStates.StatusState

trait StatusFormats {
  implicit lazy val statusJsonWrite = To[JsObject] { __ ⇒
    import play.api.data.mapping.json.Writes._
    (
      (__ \ "state").write[StatusState] ~
      (__ \ "target_url").write[Option[String]] ~
      (__ \ "description").write[Option[String]] ~
      (__ \ "context").write[Option[String]]

    )(unlift(Status.unapply _))
  }

  implicit lazy val statusJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "state").read[StatusState] ~
      (__ \ "target_url").read[Option[String]] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "context").read[Option[String]]

    )(Status.apply _)

  }
}

case class Status(
  state:       StatusState,
  targetUrl:   Option[String] = None,
  description: Option[String] = None,
  context:     Option[String] = None
)
