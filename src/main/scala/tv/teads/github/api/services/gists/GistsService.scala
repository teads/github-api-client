package tv.teads.github.api.services.gists

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class GistsService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val comments = new GistsCommentsService(config)

}
