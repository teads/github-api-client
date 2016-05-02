package tv.teads.github.api.services.gitdata

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class GitDataService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val blobs = new GitBlobsService(config)
  val commits = new GitCommitsService(config)
  val references = new GitReferencesService(config)
  val tags = new GitTagsService(config)
  val trees = new GitTreesService(config)

}
