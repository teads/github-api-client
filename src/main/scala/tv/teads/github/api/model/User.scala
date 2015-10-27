package tv.teads.github.api.model

import io.circe._

trait UserCodec {
  implicit lazy val userDecoder = Decoder.instance { cursor ⇒
    for {
      name ← cursor.downField("name").as[Option[String]]
      email ← cursor.downField("email").as[Option[String]]
      login ← cursor.downField("login").as[Option[String]]
      id ← cursor.downField("id").as[Option[Long]]
      avatarUrl ← cursor.downField("avatar_url").as[Option[String]]
      gravatarId ← cursor.downField("gravatar_id").as[Option[String]]
      url ← cursor.downField("url").as[Option[String]]
      htmlUrl ← cursor.downField("html_url").as[Option[String]]
      followersUrl ← cursor.downField("followers_url").as[Option[String]]
      followingUrl ← cursor.downField("following_url").as[Option[String]]
      gistsUrl ← cursor.downField("gists_url").as[Option[String]]
      starredUrl ← cursor.downField("starred_url").as[Option[String]]
      subscriptionsUrl ← cursor.downField("subscriptions_url").as[Option[String]]
      organizationsUrl ← cursor.downField("organizations_url").as[Option[String]]
      reposUrl ← cursor.downField("repos_url").as[Option[String]]
      eventsUrl ← cursor.downField("events_url").as[Option[String]]
      receivedEventsUrl ← cursor.downField("received_events_url").as[Option[String]]
      userType ← cursor.downField("type").as[Option[String]]
      siteAdmin ← cursor.downField("site_admin").as[Option[Boolean]]
    } yield User(
      name, email, login, id, avatarUrl, gravatarId, url, htmlUrl, followersUrl, followingUrl,
      gistsUrl, starredUrl, subscriptionsUrl, organizationsUrl, reposUrl, eventsUrl, receivedEventsUrl,
      userType, siteAdmin
    )
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
  userType:          Option[String],
  siteAdmin:         Option[Boolean]
)
