package tv.teads.github.api.model.common

import enumeratum._
import io.circe._
import tv.teads.github.api.model.JsonEnum

sealed abstract class AccountType extends EnumEntry
object AccountType extends JsonEnum[AccountType] {
  override def values: Seq[AccountType] = findValues

  case object User extends AccountType
  case object Organization extends AccountType
}

trait GithubAccountCodec {
  implicit val githubAccountDecoder = Decoder.forProduct17(
    "login", "id", "avatar_url", "gravatar_id", "url", "html_url", "followers_url",
    "following_url", "gists_url", "starred_url", "subscriptions_url", "organizations_url",
    "repos_url", "events_url", "received_events_url", "type", "site_admin"
  )(GithubAccount.apply)
}
case class GithubAccount(
  login:             String,
  id:                Long,
  avatarUrl:         String,
  gravatarId:        String,
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
  accountType:       AccountType,
  siteAdmin:         Boolean
)