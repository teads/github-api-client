package tv.teads.github.api.services

import okhttp3.Request
import io.circe._, io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._

object RepositoryService {

  implicit lazy val repoParamEncoder = Encoder.instance[RepoParam] { param ⇒
    import param._
    Json.obj(
      "name" → name.asJson,
      "description" → description.asJson,
      "private" → isPrivate.asJson,
      "has_issues" → hasIssues.asJson,
      "has_wiki" → hasWiki.asJson,
      "has_downloads" → hasDownloads.asJson,
      "team_id" → idTeam.asJson,
      "auto_init" → autoInit.asJson,
      "gitignore_template" → gitignoreTemplate.asJson,
      "license_template" → licenseTemplate.asJson,
      "default_branch" → defaultBranch.asJson
    )
  }

  case class RepoParam(
    name:              String,
    description:       String,
    homepage:          Option[String] = None,
    isPrivate:         Boolean        = false,
    hasIssues:         Boolean        = true,
    hasWiki:           Boolean        = true,
    hasDownloads:      Boolean        = true,
    idTeam:            Option[Long]   = None,
    autoInit:          Boolean        = false,
    gitignoreTemplate: Option[String] = None,
    licenseTemplate:   Option[String] = None,
    defaultBranch:     Option[String] = None
  )
}

class RepositoryService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import RepositoryService._

  def listTags(repository: String)(implicit ec: ExecutionContext): Future[List[Tag]] =
    fetchMultiple[Tag](
      s"repos/${config.owner}/$repository/tags",
      s"Fetching tags for repository $repository failed"
    )

  def fetchAllRepositories(implicit ec: ExecutionContext): Future[List[Repository]] =
    fetchAllPages[Repository](
      s"orgs/${config.owner}/repos",
      s"Fetching all repositories failed"
    )

  def listLanguages(repository: String)(implicit ec: ExecutionContext): Future[List[LanguageStat]] =
    fetchMultiple[LanguageStat](
      s"repos/${config.owner}/$repository/languages",
      s"Fetching languages for repository $repository failed"
    )

  def listContributors(repository: String)(implicit ec: ExecutionContext): Future[List[User]] =
    fetchMultiple[User](
      s"repos/${config.owner}/$repository/contributors",
      s"Fetching contributors for repository $repository failed"
    )

  def listBranches(repository: String)(implicit ec: ExecutionContext): Future[List[Branch]] =
    fetchMultiple[Branch](
      s"repos/${config.owner}/$repository/branches",
      s"Fetching branches for repository $repository failed"
    )

  def fetchBranch(repository: String, branch: String)(implicit ec: ExecutionContext): Future[Option[Branch]] =
    fetchOptional[Branch](
      s"repos/${config.owner}/$repository/branches/$branch",
      s"Fetching branch $branch for repository $repository failed"
    )

  def delete(repository: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting repo $repository failed", response.code(), false)
    }
  }

  def fetch(repository: String)(implicit ec: ExecutionContext): Future[Option[Repository]] =
    fetchOptional[Repository](
      s"repos/${config.owner}/$repository",
      s"Fetching repository $repository failed"
    )

  def create(repo: RepoParam)(implicit ec: ExecutionContext): Future[Option[Repository]] = {
    val url = s"${config.apiUrl}/orgs/${config.owner}/repos"
    val requestBuilder = new Request.Builder().url(url).post(repo.toJson)
    baseRequest(requestBuilder).map {
      _.as[Repository].fold(
        code ⇒ failedRequest(s"Creating repository $repo failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def edit(id: Long, repo: RepoParam)(implicit ec: ExecutionContext): Future[Option[Repository]] = {
    val url = s"${config.apiUrl}/repos/$id"
    val requestBuilder = new Request.Builder().url(url).patch(repo.toJson)
    baseRequest(requestBuilder).map {
      _.as[Repository].fold(
        code ⇒ failedRequest(s"Editing repository $repo failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

}
