package tv.teads.github.api.model

import io.circe._

trait MemberCodec {
  implicit lazy val memberDecoder = Decoder.instance { cursor ⇒
    for {
      login ← cursor.downField("login").as[String]
      id ← cursor.downField("id").as[Long]
      avatarUrl ← cursor.downField("avatar_url").as[Option[String]]
      gravatarId ← cursor.downField("gravatar_id").as[Option[String]]
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      followersUrl ← cursor.downField("followers_url").as[String]
      followingUrl ← cursor.downField("following_url").as[String]
      gistsUrl ← cursor.downField("gists_url").as[String]
      starredUrl ← cursor.downField("starred_url").as[String]
      subscriptionsUrl ← cursor.downField("subscriptions_url").as[String]
      organizationsUrl ← cursor.downField("organizations_url").as[String]
      reposUrl ← cursor.downField("repos_url").as[String]
      eventsUrl ← cursor.downField("events_url").as[String]
      receivedEventsUrl ← cursor.downField("received_events_url").as[String]
      memberType ← cursor.downField("type").as[String]
      isSiteAdmin ← cursor.downField("site_admin").as[Boolean]
    } yield Member(
      login, id, avatarUrl, gravatarId, url, htmlUrl, followersUrl, followingUrl, gistsUrl, starredUrl,
      subscriptionsUrl, organizationsUrl, reposUrl, eventsUrl, receivedEventsUrl, memberType, isSiteAdmin
    )
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

