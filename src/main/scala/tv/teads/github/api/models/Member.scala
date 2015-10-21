package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait MemberFormats {
  implicit lazy val memberGhJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "login").read[String] ~
      (__ \ "id").read[Long] ~
      (__ \ "avatar_url").read[Option[String]] ~
      (__ \ "gravatar_id").read[Option[String]] ~
      (__ \ "url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "followers_url").read[String] ~
      (__ \ "following_url").read[String] ~
      (__ \ "gists_url").read[String] ~
      (__ \ "starred_url").read[String] ~
      (__ \ "subscriptions_url").read[String] ~
      (__ \ "organizations_url").read[String] ~
      (__ \ "repos_url").read[String] ~
      (__ \ "events_url").read[String] ~
      (__ \ "received_events_url").read[String] ~
      (__ \ "type").read[String] ~
      (__ \ "site_admin").read[Boolean]
    )(Member.apply _)
  }

}

case class Member(
  login:             String,
  id:                Long,
  avatarUrl:         Option[String],
  gravatarId:        Option[String],
  url:               String,
  htmlUrl:           String,
  followersUrl:      String,
  followingUrl:      String,
  gistsUrl:          String,
  starredUrl:        String,
  subscriptionsUrl:  String,
  organizationsUrl:  String,
  reposUrl:          String,
  eventsUrl:         String,
  receivedEventsUrl: String,
  typ:               String,
  siteAdmin:         Boolean
)

