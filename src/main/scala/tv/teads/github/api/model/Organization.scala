package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait OrganizationCodec {
  self: ZonedDateTimeCodec ⇒

  implicit lazy val orgDecoder = Decoder.instance { cursor ⇒
    for {
      login ← cursor.downField("login").as[String]
      id ← cursor.downField("id").as[Long]
      url ← cursor.downField("url").as[String]
      reposUrl ← cursor.downField("repos_url").as[String]
      eventsUrl ← cursor.downField("events_url").as[String]
      membersUrl ← cursor.downField("members_url").as[String]
      publicMembersUrl ← cursor.downField("public_members_url").as[String]
      avatarUrl ← cursor.downField("avatar_url").as[String]
      description ← cursor.downField("description").as[Option[String]]
    } yield Org(login, id, url, reposUrl, eventsUrl, membersUrl, publicMembersUrl, avatarUrl, description)
  }

  implicit lazy val organizationDecoder = Decoder.instance { cursor ⇒
    for {
      org ← cursor.as[Org]
      name ← cursor.downField("name").as[String]
      company ← cursor.downField("company").as[Option[String]]
      blog ← cursor.downField("blog").as[Option[String]]
      location ← cursor.downField("location").as[Option[String]]
      email ← cursor.downField("email").as[Option[String]]
      publicRepos ← cursor.downField("public_repos").as[Long]
      publicGists ← cursor.downField("public_gists").as[Long]
      followers ← cursor.downField("followers").as[Long]
      following ← cursor.downField("following").as[Long]
      htmlUrl ← cursor.downField("html_url").as[String]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      orgType ← cursor.downField("type").as[String]
    } yield Organization(
      org, name, company, blog, location, email, publicRepos, publicGists,
      followers, following, htmlUrl, createdAt, updatedAt, orgType
    )
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
  createdAt:   ZonedDateTime,
  updatedAt:   ZonedDateTime,
  orgType:     String
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
