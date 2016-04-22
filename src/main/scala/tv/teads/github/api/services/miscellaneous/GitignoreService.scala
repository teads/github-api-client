package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{GitignoreTemplate, GitignoreTemplateCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

class GitignoreService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with GitignoreTemplateCodec {

  def listAll(implicit ec: ExecutionContext): Future[Set[String]] =
    get[Set[String]](
      "gitignore/templates",
      "Could not fetch all gitignore templates"
    )

  def get(templateName: String)(implicit ec: ExecutionContext): Future[Option[GitignoreTemplate]] =
    getOptional[GitignoreTemplate](
      s"gitignore/templates/$templateName",
      s"Could not fetch Gitignore template $templateName"
    )

  def getRaw(templateName: String)(implicit ec: ExecutionContext): Future[Option[String]] =
    getRawOptional(
      s"gitignore/templates/$templateName",
      s"Could not fetch Gitignore template $templateName",
      "application/vnd.github.v3.raw"
    )

}
