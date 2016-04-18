package tv.teads.github.api.services.pullrequests

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class PullRequestsService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val reviewComments = new PullRequestsReviewCommentsService(config)
}
