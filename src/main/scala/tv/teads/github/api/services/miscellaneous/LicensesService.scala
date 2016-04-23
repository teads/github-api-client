package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model.miscellaneous.{LicenseFull, LicenseFullCodec, LicenseSummary, LicenseSummaryCodec}
import tv.teads.github.api.services.AbstractGithubService

import scala.concurrent.{ExecutionContext, Future}

// TODO: get repository's license
class LicensesService(config: GithubApiClientConfig) extends AbstractGithubService(config)
    with LicenseSummaryCodec with LicenseFullCodec {

  private val licensePreviewMediaType = "application/vnd.github.drax-preview+json"

  def listAll(implicit ec: EC): Future[Set[LicenseSummary]] =
    json[Set[LicenseSummary]](
      getCall("licenses", licensePreviewMediaType),
      "Could not fetch all licenses"
    )

  def get(licenseKey: String)(implicit ec: EC): Future[Option[LicenseFull]] =
    jsonOptional[LicenseFull](
      getCall(s"licenses/$licenseKey", licensePreviewMediaType),
      s"Could not fetch license $licenseKey"
    )

}
