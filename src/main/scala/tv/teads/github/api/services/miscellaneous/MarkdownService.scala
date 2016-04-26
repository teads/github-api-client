package tv.teads.github.api.services.miscellaneous

import io.circe.generic.auto._
import okhttp3.{MediaType, RequestBody}
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.MarkdownRenderingRequest
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

class MarkdownService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  def render(request: MarkdownRenderingRequest)(implicit ec: EC): Future[String] =
    raw(
      postCall(
        "markdown",
        body = jsonRequestBody(request)
      )
    )

  def renderRaw(text: String)(implicit ec: EC): Future[String] =
    raw(
      postCall(
        "markdown/raw",
        body = RequestBody.create(MediaType.parse("text/x-markdown"), text)
      )
    )

}
