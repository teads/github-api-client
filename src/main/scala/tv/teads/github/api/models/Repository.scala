package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait RepositoryUrlsFormats {
    implicit lazy val  repositoryUrlsJsonWrite : Write[RepositoryUrls, JsValue] = {
      import play.api.data.mapping.json.Writes._
      Write.gen[RepositoryUrls, JsObject]
    }

  implicit lazy val  repositoryUrlsJsonRead =  From[JsValue] { __ =>
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "url").read[String] ~
        (__ \ "html_url").read[String] ~
        (__ \ "clone_url").read[String] ~
        (__ \ "git_url").read[String] ~
        (__ \ "ssh_url").read[String] ~
        (__ \ "svn_url").read[String] ~
        (__ \ "mirror_url").read[String]
      )(RepositoryUrls.apply _)
  }

}
case class RepositoryUrls(url: String,
                          html_url: String,
                          clone_url: String,
                          git_url: String,
                          ssh_url: String,
                          svn_url: String,
                          mirror_url: String)
trait PermissionFormats {
    implicit lazy val  permissionsJsonWrite : Write[Permissions, JsValue] = {
      import play.api.data.mapping.json.Writes._
      Write.gen[Permissions, JsObject]
    }

  implicit lazy val  permissionJsonRead =  From[JsValue] { __ =>
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "admin").read[Boolean] ~
        (__ \ "push").read[Boolean] ~
        (__ \ "pull").read[Boolean]
      )(Permissions.apply _)
  }

}
case class Permissions(
                        admin: Boolean,
                        push: Boolean,
                        pull: Boolean
                        )

trait RepositoryStatsFormats {
    implicit lazy val  repositoryStatsJsonWrite : Write[RepositoryStats, JsValue] = {
      import play.api.data.mapping.json.Writes._
      Write.gen[RepositoryStats, JsObject]
    }

  implicit lazy val  repositoryStatsJsonRead =  From[JsValue] { __ =>
    import play.api.data.mapping.json.Rules._
    (
      (__ \ "forks_count").read[Long] ~
        (__ \ "stargazers_count").read[Long] ~
        (__ \ "watchers_count").read[Long] ~
        (__ \ "size").read[Long] ~
        (__ \ "open_issues_count").read[Long] ~
        (__ \ "subscribers_count").read[Long]
      )(RepositoryStats.apply _)
  }

}
case class RepositoryStats(
                            forks_count: Long,
                            stargazers_count: Long,
                            watchers_count: Long,
                            size: Long,
                            open_issues_count: Long,
                            subscribers_count: Long
                            )

trait RepositoryConfigFormats {
    implicit lazy val  repositoryConfigJsonWrite : Write[RepositoryConfig, JsValue] = {
      import play.api.data.mapping.json.Writes._
      Write.gen[RepositoryConfig, JsObject]
    }

  implicit lazy val  repositoryConfigJsonRead =  From[JsValue] { __ =>
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
                            has_issues: Boolean,
                            has_wiki: Boolean,
                            has_pages: Boolean,
                            has_downloads: Boolean
                            )


trait RepositoryFormats { 
  self:UserFormats  with RepositoryUrlsFormats with PermissionFormats with RepositoryConfigFormats with RepositoryStatsFormats  with ParentFormats =>

  implicit lazy val  repositoryJsonWrite: Write[Repository, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Repository, JsObject]
  }

  private val TagsPrefix = "tags["

  implicit lazy val  GHRepoReads: Rule[JsValue, Repository] = From[JsValue] { __ =>
    import play.api.data.mapping.json.Rules._

    val tagsR = Rule.fromMapping[Option[String], List[String]]{
      case None =>  Success(Nil)
      case Some(str) =>
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
        (__ \ "homepage").read[String] ~
        (__ \ "language").read[String] ~
        (__ \ "default_branch").read[String] ~
        (__ \ "pushed_at").read[String] ~
        (__ \ "created_at").read[String] ~
        (__ \ "updated_at").read[String] ~
        (__ \ "permissions").read[Permissions] ~
        (__ \ "organization").read[User] ~
        (__ \ "parent").read[Parent] ~
        (__ \ "source").read[Parent] ~
        repositoryUrlsJsonRead ~
        repositoryStatsJsonRead ~
        repositoryConfigJsonRead ~
        (__ \ "description").read(tagsR)
      ) (Repository.apply _)
  }
}

case class Repository(
                       id: Long,
                       name: String,
                       full_name: String,
                       owner: User,
                       `private`: Boolean,
                       description: Option[String],
                       fork: Boolean,
                       homepage: String,
                       language: String,
                       default_branch: String,
                       pushed_at: String,
                       created_at: String,
                       updated_at: String,
                       permissions: Permissions,
                       organization: User,
                       parent: Parent,
                       source: Parent,
                       urls: RepositoryUrls,
                       stats: RepositoryStats,
                       config: RepositoryConfig,
                       tags: List[String] = Nil
                       )
