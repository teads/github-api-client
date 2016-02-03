package tv.teads.github.api

import java.io.File

import tv.teads.github.api.http.{OkHttpClientWrapper, Authenticator}
import tv.teads.github.api.services._

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
  val contents = new ContentService(config)
  val hooks = new HookService(config)
  val issues = new IssueService(config)
  val labels = new LabelService(config)
  val members = new MemberService(config)
  val organizations = new OrganizationService(config)
  val pullRequests = new PullRequestService(config)
  val rateLimit = new RateLimitService(config)
  val repositories = new RepositoryService(config)
  val statuses = new StatusService(config)
  val teams = new TeamService(config)
  val users = new UserService(config)
  val releases = new ReleaseService(config)
  val commits = new CommitService(config)

  def clearCache(): Unit = config.client.clearCache()
}
