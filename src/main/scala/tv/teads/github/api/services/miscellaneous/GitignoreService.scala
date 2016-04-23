package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{GitignoreTemplate, GitignoreTemplateCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class GitignoreService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with GitignoreTemplateCodec {

  def listAll(implicit ec: EC): Future[Set[String]] =
    json[Set[String]](
      getCall("gitignore/templates"),

      "Could not fetch all gitignore templates"
    )

  def get(templateName: String)(implicit ec: EC): Future[Option[GitignoreTemplate]] =
    jsonOptional[GitignoreTemplate](
      getCall(s"gitignore/templates/$templateName"),
      s"Could not fetch Gitignore template $templateName"
    )

  def getRaw(templateName: String)(implicit ec: EC): Future[Option[String]] =
    rawOptional(
      getCall(s"gitignore/templates/$templateName", "application/vnd.github.v3.raw"),
      s"Could not fetch Gitignore template $templateName"
    )

}
