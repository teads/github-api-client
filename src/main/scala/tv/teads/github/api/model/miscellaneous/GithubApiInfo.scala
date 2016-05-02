package tv.teads.github.api.model.miscellaneous

import io.circe._

object GithubApiInfo {
  implicit final val githubApiInfoDecoder =
    Decoder.forProduct6(
      "verifiable_password_authentication", "github_services_sha",
      "hooks", "git", "pages", "importer"
    )(GithubApiInfo.apply)
}
case class GithubApiInfo(
  verifiablePasswordAuthentication: Boolean,
  githubServicesSha:                String,
  hooks:                            List[String],
  git:                              List[String],
  pages:                            List[String],
  importer:                         List[String]
)