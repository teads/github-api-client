package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait OrganizationFormats {
  implicit lazy val orgJsonWrite: Write[Org, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[Org, JsObject]
  }

  implicit lazy val organizationJsonWrite: Write[Organization, JsValue] = {
    import play.api.data.mapping.json.Writes._
    import tv.teads.github.api.util.CustomWrites._
    Write.gen[Organization, JsObject]
  }

  implicit lazy val orgJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "login").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "url").read[String] ~
      (__ \ "repos_url").read[String] ~
      (__ \ "events_url").read[String] ~
      (__ \ "members_url").read[String] ~
      (__ \ "public_members_url").read[String] ~
      (__ \ "avatar_url").read[String] ~
      (__ \ "description").read[Option[String]]
    )(Org.apply _)
  }

  implicit lazy val organizationJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      orgJsonRead ~
      (__ \ "name").read[String] ~
      (__ \ "company").read[Option[String]] ~
      (__ \ "blog").read[Option[String]] ~
      (__ \ "location").read[Option[String]] ~
      (__ \ "email").read[Option[String]] ~
      (__ \ "public_repos").read[Long] ~
      (__ \ "public_gists").read[Long] ~
      (__ \ "followers").read[Long] ~
      (__ \ "following").read[Long] ~
      (__ \ "html_url").read[String] ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "type").read[String]
    )(Organization.apply _)
  }

}

case class Organization(
  org:         Org,
  name:        String,
  company:     Option[String],
  blog:        Option[String],
  location:    Option[String],
  email:       Option[String],
  publicRepos: Long,
  publicGists: Long,
  followers:   Long,
  following:   Long,
  htmlUrl:     String,
  createdAt:   DateTime,
  updatedAt:   DateTime,
  typ:         String
)

case class Org(
  login:            String,
  id:               Long,
  url:              String,
  reposUrl:         String,
  eventsUrl:        String,
  membersUrl:       String,
  publicMembersUrl: String,
  avatarUrl:        String,
  description:      Option[String]
)