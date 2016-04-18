package tv.teads.github.api.services

import com.typesafe.scalalogging.StrictLogging
import tv.teads.github.api.GithubApiClientConfig

abstract class AbstractGithubService(config: GithubApiClientConfig) extends StrictLogging {
  protected val DefaultMediaType = "application/vnd.github.v3+json"

}
