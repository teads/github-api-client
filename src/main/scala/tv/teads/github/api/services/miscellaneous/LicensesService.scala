package tv.teads.github.api.services.miscellaneous

import io.circe.generic.auto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{LicenseFull, LicenseSummary}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.Future

// TODO: get repository's license
class LicensesService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  private val licensePreviewMediaType = "application/vnd.github.drax-preview+json"

  def listAll(implicit ec: EC): Future[List[LicenseSummary]] =
    json[List[LicenseSummary]](
      getCall("licenses", licensePreviewMediaType),
      "Could not fetch all licenses"
    )

  def get(licenseKey: String)(implicit ec: EC): Future[Option[LicenseFull]] =
    jsonOptionalIfFailed[LicenseFull](
      getCall(s"licenses/$licenseKey", licensePreviewMediaType),
      s"Could not fetch license $licenseKey"
    )

}
