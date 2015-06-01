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

  protected def baseRequest(path: String,
                            queryParams: Map[String, String],
                            useTestMediaType: Boolean = false,
                            paginated: Boolean = false) = {
    val uri = Uri(path)
    val paramsWithToken = queryParams + configuration.api.tokenHeader
    val fullParams = if (paginated) paramsWithToken + configuration.api.paginationHeader else paramsWithToken
    val mediaType = if (useTestMediaType) TestMediaType else DefaultMediaType

    HttpClient(uri.withQuery(fullParams ++ uri.query))
      .withHeader(HttpHeaders.Accept.name, mediaType)
  }

}
