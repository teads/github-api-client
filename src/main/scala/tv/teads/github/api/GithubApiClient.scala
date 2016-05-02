package tv.teads.github.api

import java.io.File

import tv.teads.github.api.http.{Authenticator, OkHttpClientWrapper}
import tv.teads.github.api.services.gists.GistsService
import tv.teads.github.api.services.gitdata.GitDataService
import tv.teads.github.api.services.issues.IssuesService
import tv.teads.github.api.services.miscellaneous.MiscellaneousService
import tv.teads.github.api.services.organizations.OrganizationsService
import tv.teads.github.api.services.pullrequests.PullRequestsService
import tv.teads.github.api.services.repositories.RepositoriesService
import tv.teads.github.api.services.search.SearchService
import tv.teads.github.api.services.users.UsersService

private[api] case class GithubApiClientConfig(
    owner:         String,
    apiUrl:        String,
    authenticator: Option[Authenticator],
    itemsPerPage:  Int,
    maxCacheSize:  Long,
    cacheRoot:     File
) {
  val client = new OkHttpClientWrapper(authenticator, maxCacheSize, cacheRoot)
}

case class GithubApiClient private[api] (config: GithubApiClientConfig) {

  val gists = new GistsService(config)
  val gitData = new GitDataService(config)
  val issues = new IssuesService(config)
  val miscellaneous = new MiscellaneousService(config)
  val organizations = new OrganizationsService(config)
  val pullRequests = new PullRequestsService(config)
  val repositories = new RepositoriesService(config)
  val search = new SearchService(config)
  val users = new UsersService(config)

  def clearCache(): Unit = config.client.clearCache()
}
