package tv.teads.github.api.models

import play.api.libs.json.JsValue
import play.api.libs.json._
import play.api.data.mapping._
import play.api.libs.functional.syntax._

import tv.teads.github.api.models.Privacies.Privacy
import tv.teads.github.api.models.Permissions.Permission

trait TeamFormats {
  implicit lazy val teamJsonWrite = To[JsObject] { __ ⇒
    import play.api.data.mapping.json.Writes._
    // let's no leak implicits everywhere
    (
      (__ \ "name").write[String] ~
      (__ \ "id").write[Long] ~
      (__ \ "slug").write[String] ~
      (__ \ "privacy").write[Option[Privacy]] ~
      (__ \ "description").write[Option[String]] ~
      (__ \ "permission").write[Permission] ~
      (__ \ "url").write[String] ~
      (__ \ "members_url").write[String] ~
      (__ \ "repositories_url").write[String]
    )(unlift(Team.unapply _))
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
