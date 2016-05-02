package tv.teads.github.api.services.organizations

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class OrganizationsService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val members = new OrganizationMembersService(config)
  val teams = new OrganizationTeamsService(config)
  val webhooks = new OrganizationWebhooksService(config)
}
