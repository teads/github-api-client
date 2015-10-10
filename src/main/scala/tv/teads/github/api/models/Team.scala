package tv.teads.github.api.models
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._
import tv.teads.github.api.models.Privacies.Privacy
import tv.teads.github.api.models.Permissions.Permission

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
      (__ \ "privacy").read[Option[Privacy]] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "permission").read[Permission] ~
      (__ \ "url").read[String] ~
      (__ \ "members_url").read[String] ~
      (__ \ "repositories_url").read[String]
    )(Team.apply _)
  }

}
case class Team(
  name:            String,
  id:              Long,
  slug:            String,
  privacy:         Option[Privacy],
  description:     Option[String],
  permission:      Permission,
  url:             String,
  membersUrl:      String,
  repositoriesUrl: String
)
