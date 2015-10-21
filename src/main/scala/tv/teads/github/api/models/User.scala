package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait UserFormats {

  implicit lazy val userJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "name").read[Option[String]] ~
      (__ \ "email").read[Option[String]] ~
      (__ \ "login").read[Option[String]] ~
      (__ \ "id").read[Option[Long]] ~
      (__ \ "avatar_url").read[Option[String]] ~
      (__ \ "gravatar_id").read[Option[String]] ~
      (__ \ "url").read[Option[String]] ~
      (__ \ "html_url").read[Option[String]] ~
      (__ \ "followers_url").read[Option[String]] ~
      (__ \ "following_url").read[Option[String]] ~
      (__ \ "gists_url").read[Option[String]] ~
      (__ \ "starred_url").read[Option[String]] ~
      (__ \ "subscriptions_url").read[Option[String]] ~
      (__ \ "organizations_url").read[Option[String]] ~
      (__ \ "repos_url").read[Option[String]] ~
      (__ \ "events_url").read[Option[String]] ~
      (__ \ "receivend_events_url").read[Option[String]] ~
      (__ \ "type").read[Option[String]] ~
      (__ \ "site_admin").read[Option[Boolean]]
    )(User.apply _)
  }

}

case class User(
  name:              Option[String],
  email:             Option[String],
  login:             Option[String],
  id:                Option[Long],
  avatarUrl:         Option[String],
  gravatarId:        Option[String],
  url:               Option[String],
  htmlUrl:           Option[String],
  followersUrl:      Option[String],
  followingUrl:      Option[String],
  gistsUrl:          Option[String],
  starredUrl:        Option[String],
  subscriptionsUrl:  Option[String],
  organizationsUrl:  Option[String],
  reposUrl:          Option[String],
  eventsUrl:         Option[String],
  receivedEventsUrl: Option[String],
  typ:               Option[String],
  siteAdmin:         Option[Boolean]
)