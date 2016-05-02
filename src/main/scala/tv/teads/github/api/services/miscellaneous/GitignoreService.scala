package tv.teads.github.api.services.miscellaneous

import io.circe.generic.auto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.GitignoreTemplate
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class GitignoreService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def listAll(implicit ec: EC): Future[List[String]] =
    json[List[String]](
      getCall("gitignore/templates"),

      "Could not fetch all gitignore templates"
    )

  def get(templateName: String)(implicit ec: EC): Future[Option[GitignoreTemplate]] =
    jsonOptionalIfFailed[GitignoreTemplate](
      getCall(s"gitignore/templates/$templateName"),
      s"Could not fetch Gitignore template $templateName"
    )

  def getRaw(templateName: String)(implicit ec: EC): Future[Option[String]] =
    rawOptional(
      getCall(s"gitignore/templates/$templateName", "application/vnd.github.v3.raw"),
      s"Could not fetch Gitignore template $templateName"
    )

}
