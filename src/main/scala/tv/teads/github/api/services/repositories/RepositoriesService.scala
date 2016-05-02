package tv.teads.github.api.services.repositories

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class RepositoriesService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val collaborators = new RepositoriesCollaboratorsService(config)
  val comments = new RepositoriesCollaboratorsService(config)
  val commits = new RepositoriesCommitsService(config)
  val contents = new RepositoriesContentsService(config)
  val deployKeys = new RepositoriesDeployKeysService(config)
  val deployments = new RepositoriesDeploymentsService(config)
  val downloads = new RepositoriesDownloadsService(config)
  val forks = new RepositoriesForksService(config)
  val merging = new RepositoriesMergingService(config)
  val pages = new RepositoriesPagesService(config)
  val releases = new RepositoriesReleasesService(config)
  val statistics = new RepositoriesStatisticsService(config)
  val statuses = new RepositoriesStatisticsService(config)
  val webhooks = new RepositoriesWebhooksService(config)

}
