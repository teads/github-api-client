package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class EmojisService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAll(implicit ec: EC): Future[Map[String, String]] =
    json[Map[String, String]](
      getCall("emojis"),
      "Could not fetch all emojis"
    )

}
