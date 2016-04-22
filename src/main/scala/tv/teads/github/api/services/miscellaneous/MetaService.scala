package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{GithubApiInfo, GithubApiInfoCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

class MetaService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with GithubApiInfoCodec {

  def get(implicit ec: ExecutionContext): Future[GithubApiInfo] =
    json[GithubApiInfo](
      getCall("meta"),
      "Could not fetch Github API information"
    )

}
