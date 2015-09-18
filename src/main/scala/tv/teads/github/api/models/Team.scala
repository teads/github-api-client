package tv.teads.github.api.models
import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait TeamFormats {
  implicit lazy val teamJsonWrite: Write[Team, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Team, JsObject]
  }

  implicit lazy val teamJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "name").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "slug").read[String] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "permission").read[Option[String]] ~
      (__ \ "url").read[String] ~
      (__ \ "members_url").read[String] ~
      (__ \ "repositories_url").read[String]
    )(Team.apply _)
  }

}
case class Team(
  name:             String,
  id:               Long,
  slug:             String,
  description:      Option[String],
  permission:       Option[String],
  url:              String,
  membersUrl:      String,
  repositoriesUrl: String
)
