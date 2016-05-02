package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class MiscellaneousService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val emojis = new EmojisService(config)
  val gitignore = new GitignoreService(config)
  val licenses = new LicensesService(config)
  val markdown = new MarkdownService(config)
  val meta = new MetaService(config)
  val rateLimit = new RateLimitService(config)

}
