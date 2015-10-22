package tv.teads.github.api.models

import java.time.ZonedDateTime

import play.api.data.mapping._
import play.api.libs.json.JsValue

trait RepositoryUrlsFormats {
  implicit lazy val repositoryUrlsJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "clone_url").read[String] ~
      (__ \ "git_url").read[String] ~
      (__ \ "ssh_url").read[String] ~
      (__ \ "svn_url").read[String]

    )(RepositoryUrls.apply _)
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

trait BooleanPermissionFormats {
  implicit lazy val permissionJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "admin").read[Boolean] ~
      (__ \ "push").read[Boolean] ~
      (__ \ "pull").read[Boolean]
    )(BooleanPermissions.apply _)
  }

}
case class BooleanPermissions(
  admin: Boolean,
  push:  Boolean,
  pull:  Boolean
)

trait RepositoryStatsFormats {
  implicit lazy val repositoryStatsJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "forks_count").read[Long] ~
      (__ \ "stargazers_count").read[Long] ~
      (__ \ "watchers_count").read[Long] ~
      (__ \ "size").read[Long] ~
      (__ \ "open_issues_count").read[Long] ~
      (__ \ "watchers").read[Long]
    )(RepositoryStats.apply _)
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

trait RepositoryConfigFormats {
  implicit lazy val repositoryConfigJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "has_issues").read[Boolean] ~
      (__ \ "has_wiki").read[Boolean] ~
      (__ \ "has_pages").read[Boolean] ~
      (__ \ "has_downloads").read[Boolean]
    )(RepositoryConfig.apply _)
  }

}
case class RepositoryConfig(
  hasIssues:    Boolean,
  hasWiki:      Boolean,
  hasPages:     Boolean,
  hasDownloads: Boolean
)

trait RepositoryFormats {
  self: UserFormats with RepositoryUrlsFormats with BooleanPermissionFormats with RepositoryConfigFormats with RepositoryStatsFormats with ParentFormats ⇒

  implicit lazy val GHRepoReads: Rule[JsValue, Repository] = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._

    (
      (__ \ "id").read[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "full_name").read[String] ~
      (__ \ "owner").read[User] ~
      (__ \ "private").read[Boolean] ~
      (__ \ "description").read[Option[String]] ~
      (__ \ "fork").read[Boolean] ~
      (__ \ "homepage").read[Option[String]] ~
      (__ \ "language").read[Option[String]] ~
      (__ \ "default_branch").read[String] ~
      (__ \ "pushed_at").read(zonedDateTime) ~
      (__ \ "created_at").read(zonedDateTime) ~
      (__ \ "updated_at").read(zonedDateTime) ~
      (__ \ "permissions").read[Option[BooleanPermissions]] ~
      (__ \ "organization").read[Option[User]] ~
      repositoryUrlsJsonRead ~
      repositoryStatsJsonRead ~
      repositoryConfigJsonRead
    )(Repository.apply _)
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
