package tv.teads.github.api.models

import play.api.libs.json.JsValue
import play.api.libs.json._
import play.api.data.mapping._
import play.api.libs.functional.syntax._
import tv.teads.github.api.models.StatusStates.StatusState

trait CombinedStatusFormats {
  self: StatusFormats with RepositoryFormats ⇒

  implicit lazy val combinedStatusJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "state").read[StatusState] ~
      (__ \ "sha").read[String] ~
      (__ \ "total_count").read[Long] ~
      (__ \ "statuses").read[List[Status]] ~
      (__ \ "commit_url").read[String] ~
      (__ \ "url").read[String]

    )(CombinedStatus.apply _)

  }
}

case class CombinedStatus(
  state:      StatusState,
  sha:        String,
  totalCount: Long,
  statuses:   List[Status],
  commitUrl:  String,
  url:        String
)
