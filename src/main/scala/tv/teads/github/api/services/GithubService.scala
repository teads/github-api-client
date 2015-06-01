package tv.teads.github.api.services


import spray.http._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.util._
import tv.teads.github.api.services.GithubConfiguration.configuration


trait GithubService extends Service with PayloadFormats {

  protected val DefaultMediaType = "application/vnd.github.v3+json"
  protected val TestMediaType = "application/vnd.github.moondragon+json"
  protected val RawContentMediaType = "application/vnd.github.v3.raw"
  protected val PagesNavRegex = """(?:\s*)<(.+)>; rel=(.+)""".r

  protected def baseRequest(req: HttpRequest,
                            queryParams: Map[String, String],
                            useTestMediaType: Boolean = false,
                            paginated: Boolean = false) = {
    val paramsWithToken = queryParams + configuration.api.tokenHeader
    val fullParams = if (paginated) paramsWithToken + configuration.api.paginationHeader else paramsWithToken
    val mediaType = if (useTestMediaType) TestMediaType else DefaultMediaType
    val uri = req.uri.withQuery(fullParams ++ req.uri.query)
    HttpClient(req.copy(uri = uri))
      .withHeader(HttpHeaders.Accept.name, mediaType)
  }

}
