package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

class EmojisService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAll(implicit ec: ExecutionContext): Future[Map[String, String]] =
    get[Map[String, String]](
      "emojis",
      "Could not fetch all emojis"
    )

}
