package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe.Decoder
import tv.teads.github.api.json.ZonedDateTimeCodec

trait RepositoryPushCodec {
  self: UserCodec with RepositoryUrlsCodec with BooleanPermissionCodec with RepositoryConfigCodec with RepositoryStatsCodec with ZonedDateTimeCodec ⇒

  implicit lazy val repositoryPushDecoder = Decoder.instance { cursor ⇒
    for {
      id ← cursor.downField("id").as[Long]
      name ← cursor.downField("name").as[String]
      fullName ← cursor.downField("full_name").as[String]
      owner ← cursor.downField("owner").as[User]
      isPrivate ← cursor.downField("private").as[Boolean]
      description ← cursor.downField("description").as[Option[String]]
      isFork ← cursor.downField("fork").as[Boolean]
      homePage ← cursor.downField("homepage").as[Option[String]]
      language ← cursor.downField("language").as[Option[String]]
      defaultBranch ← cursor.downField("default_branch").as[String]
      pushedAt ← cursor.downField("pushed_at").as[ZonedDateTime]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      permissions ← cursor.downField("permissions").as[Option[BooleanPermissions]]
      organization ← cursor.downField("organization").as[Option[String]]
      urls ← cursor.as[RepositoryUrls]
      stats ← cursor.as[RepositoryStats]
      config ← cursor.as[RepositoryConfig]
    } yield RepositoryPush(
      id, name, fullName, owner, isPrivate, description, isFork, homePage, language, defaultBranch,
      pushedAt, createdAt, updatedAt, permissions, organization, urls, stats, config
    )
  }
}

case class RepositoryPush(
  id:            Long,
  name:          String,
  fullName:      String,
  owner:         User,
  isPrivate:     Boolean,
  description:   Option[String],
  isFork:        Boolean,
  homepage:      Option[String],
  language:      Option[String],
  defaultBranch: String,
  pushedAt:      ZonedDateTime,
  createdAt:     ZonedDateTime,
  updatedAt:     ZonedDateTime,
  permissions:   Option[BooleanPermissions],
  organization:  Option[String],
  urls:          RepositoryUrls,
  stats:         RepositoryStats,
  config:        RepositoryConfig
)
