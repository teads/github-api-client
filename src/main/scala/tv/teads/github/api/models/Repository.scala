package tv.teads.github.api.models

import org.joda.time.DateTime
import play.api.data.mapping._
import play.api.libs.json.{ JsNumber, JsObject, JsValue }
import tv.teads.github.api.models.Permissions.Permission

trait RepositoryUrlsFormats {
  implicit lazy val repositoryUrlsJsonWrite: Write[RepositoryUrls, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[RepositoryUrls, JsObject]
  }

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
//forks_url: String,
//keys_url: String,
//collaborators_url: String,
//teams_url: String,
//hooks_url: String,
//issue_events_url: String,
//events_url: String,
//assignees_url: String,
//branches_url: String,
//tags_url: String,
//blobs_url: String,
//git_tags_url: String,
//git_refs_url: String,
//trees_url: String,
//statuses_url: String,
//languages_url: String,
//stargazers_url: String,
//contributors_url: String,
//subscribers_url: String,
//subscription_url: String,
//commits_url: String,
//git_commits_url: String,
//comments_url: String,
//issue_comment_url: String,
//contents_url: String,
//compare_url: String,
//merges_url: String,
//archive_url: String,
//downloads_url: String,
//issues_url: String,
//pulls_url: String,
//milestones_url: String,
//notifications_url: String,
//labels_url: String,
//releases_url: String
)

trait BooleanPermissionFormats {
  implicit lazy val permissionsJsonWrite: Write[BooleanPermissions, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[BooleanPermissions, JsObject]
  }

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
  implicit lazy val repositoryStatsJsonWrite: Write[RepositoryStats, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[RepositoryStats, JsObject]
  }

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
  forksCount:       Long,
  stargazersCount:  Long,
  watchersCount:    Long,
  size:             Long,
  open_issuesCount: Long,
  watchers:         Long
)

trait RepositoryConfigFormats {
  implicit lazy val repositoryConfigJsonWrite: Write[RepositoryConfig, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[RepositoryConfig, JsObject]
  }

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

  implicit lazy val repositoryJsonWrite: Write[Repository, JsValue] = {
    import play.api.data.mapping.json.Writes._
    implicit val dateTimeToLongJsObject: Write[DateTime, JsValue] = Write[DateTime, JsValue] { dt ⇒ JsNumber(dt.getMillis()) }
    Write.gen[Repository, JsObject]
  }

  private val TagsPrefix = "tags["

  implicit lazy val GHRepoReads: Rule[JsValue, Repository] = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._

    val tagsR = Rule.fromMapping[Option[String], List[String]] {
      case None ⇒ Success(Nil)
      case Some(str) ⇒
        if (str.contains(TagsPrefix)) {
          val tags = str.substring(str.indexOf(TagsPrefix) + TagsPrefix.length, str.indexOf("]"))
          Success(tags.split(",").toList)
        } else {
          Success(Nil)
        }
    }

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
      (__ \ "pushed_at").read(jodaLongOrISO) ~
      (__ \ "created_at").read(jodaLongOrISO) ~
      (__ \ "updated_at").read(jodaLongOrISO) ~
      (__ \ "permissions").read[Option[BooleanPermissions]] ~
      (__ \ "organization").read[Option[User]] ~
      repositoryUrlsJsonRead ~
      repositoryStatsJsonRead ~
      repositoryConfigJsonRead ~
      (__ \ "description").read(tagsR)
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
  pushedAt:      DateTime,
  createdAt:     DateTime,
  updatedAt:     DateTime,
  permissions:   Option[BooleanPermissions],
  organization:  Option[User],
  urls:          RepositoryUrls,
  stats:         RepositoryStats,
  config:        RepositoryConfig,
  tags:          List[String]               = Nil
)
