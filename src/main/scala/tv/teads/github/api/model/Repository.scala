package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._

import tv.teads.github.api.json.ZonedDateTimeCodec

trait RepositoryUrlsCodec {
  implicit lazy val repositoryUrlsDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      cloneUrl ← cursor.downField("clone_url").as[String]
      gitUrl ← cursor.downField("git_url").as[String]
      sshUrl ← cursor.downField("ssh_url").as[String]
      svnUrl ← cursor.downField("svn_url").as[String]
    } yield RepositoryUrls(url, htmlUrl, cloneUrl, gitUrl, sshUrl, svnUrl)
  }
}
case class RepositoryUrls(
  url:      String,
  htmlUrl:  String,
  cloneUrl: String,
  gitUrl:   String,
  sshUrl:   String,
  svnUrl:   String
)

trait BooleanPermissionCodec {
  implicit lazy val permissionsDecoder = Decoder.instance { cursor ⇒
    for {
      admin ← cursor.downField("admin").as[Boolean]
      push ← cursor.downField("push").as[Boolean]
      pull ← cursor.downField("pull").as[Boolean]
    } yield BooleanPermissions(admin, push, pull)
  }
}
case class BooleanPermissions(
  admin: Boolean,
  push:  Boolean,
  pull:  Boolean
)

trait RepositoryStatsCodec {
  implicit lazy val repositoryStatsDecoder = Decoder.instance { cursor ⇒
    for {
      forksCount ← cursor.downField("forks_count").as[Long]
      stargazersCount ← cursor.downField("stargazers_count").as[Long]
      watchersCount ← cursor.downField("watchers_count").as[Long]
      size ← cursor.downField("size").as[Long]
      openIssuesCount ← cursor.downField("open_issues_count").as[Long]
      watchers ← cursor.downField("watchers").as[Long]
    } yield RepositoryStats(forksCount, stargazersCount, watchersCount, size, openIssuesCount, watchers)
  }
}
case class RepositoryStats(
  forksCount:      Long,
  stargazersCount: Long,
  watchersCount:   Long,
  size:            Long,
  openIssuesCount: Long,
  watchers:        Long
)

trait RepositoryConfigCodec {
  implicit lazy val repositoryConfigDecoder = Decoder.instance { cursor ⇒
    for {
      hasIssues ← cursor.downField("has_issues").as[Boolean]
      hasWiki ← cursor.downField("has_wiki").as[Boolean]
      hasPages ← cursor.downField("has_pages").as[Boolean]
      hasDownloads ← cursor.downField("has_downloads").as[Boolean]
    } yield RepositoryConfig(hasIssues, hasWiki, hasPages, hasDownloads)
  }
}
case class RepositoryConfig(
  hasIssues:    Boolean,
  hasWiki:      Boolean,
  hasPages:     Boolean,
  hasDownloads: Boolean
)

trait RepositoryCodec {
  self: UserCodec with RepositoryUrlsCodec with BooleanPermissionCodec with RepositoryConfigCodec with RepositoryStatsCodec with ZonedDateTimeCodec ⇒

  implicit lazy val repositoryDecoder = Decoder.instance { cursor ⇒
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
      organization ← cursor.downField("organization").as[Option[User]]
      urls ← cursor.as[RepositoryUrls]
      stats ← cursor.as[RepositoryStats]
      config ← cursor.as[RepositoryConfig]
    } yield Repository(
      id, name, fullName, owner, isPrivate, description, isFork, homePage, language, defaultBranch,
      pushedAt, createdAt, updatedAt, permissions, organization, urls, stats, config
    )
  }
}

case class Repository(
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
  organization:  Option[User],
  urls:          RepositoryUrls,
  stats:         RepositoryStats,
  config:        RepositoryConfig
)
