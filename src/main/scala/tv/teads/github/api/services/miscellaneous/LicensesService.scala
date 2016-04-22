package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{LicenseFull, LicenseFullCodec, LicenseSummary, LicenseSummaryCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

// TODO: get repository's license
class LicensesService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with LicenseSummaryCodec with LicenseFullCodec {

  private val licensePreviewMediaType = "application/vnd.github.drax-preview+json"

  def listAll(implicit ec: ExecutionContext): Future[Set[LicenseSummary]] =
    get[Set[LicenseSummary]](
      "licenses",
      "Could not fetch all licenses",
      licensePreviewMediaType
    )

  def get(licenseKey: String)(implicit ec: ExecutionContext): Future[Option[LicenseFull]] =
    getOptional[LicenseFull](
      s"licenses/$licenseKey",
      s"Could not fetch license $licenseKey",
      licensePreviewMediaType
    )

}
