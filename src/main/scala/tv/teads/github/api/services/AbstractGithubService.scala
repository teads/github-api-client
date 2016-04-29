package tv.teads.github.api.services

import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import io.circe.{Decoder, Encoder, Printer}
import okhttp3.{HttpUrl, MediaType, Request, RequestBody}
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http.ResponseWrapper

import scala.concurrent.{ExecutionContext, Future}

private[services] abstract class AbstractGithubService(config: GithubApiClientConfig) extends StrictLogging {

  protected type EC = ExecutionContext
  protected type FutureResponse = Future[ResponseWrapper]
  protected type RequestBuilderF = Request.Builder ⇒ Request.Builder

  private val JsonPrinter = Printer(preserveOrder = true, dropNullKeys = true, indent = "")
  private val DefaultMediaType = "application/vnd.github.v3+json"
  private val PagesNavRegex = """(?:\s*)<(.+)>; rel="(.+)"""".r

  private def emptyRequestBody = RequestBody.create(null, new Array[Byte](0))

  private def baseRequestBuilder(route: String, mediaType: String, params: Map[String, String]): Request.Builder = {
    def addAllParams(builder: HttpUrl.Builder, params: Map[String, String]) =
      params.foldLeft(builder) { case (b, (k, v)) ⇒ b.addEncodedQueryParameter(k, v) }

    val url = addAllParams(HttpUrl.parse(s"${config.apiUrl}/$route").newBuilder, params).toString
    new Request.Builder().url(url).addHeader("Accept", mediaType)
  }

  private def exec(
    route:     String,
    mediaType: String,
    params:    Map[String, String],
    f:         RequestBuilderF
  ): FutureResponse = config.client.executeAsync(f(baseRequestBuilder(route, mediaType, params)))

  protected def jsonRequestBody[T: Encoder](json: T): RequestBody =
    RequestBody.create(
      MediaType.parse("application/json"),
      Encoder[T].apply(json).pretty(JsonPrinter)
    )

  //////////////////
  // HTTP METHODS //
  //////////////////

  protected def getCall(
    route:     String,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty
  ): FutureResponse = exec(route, mediaType, params, _.get())

  protected def postCall(
    route:     String,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty,
    body:      RequestBody         = emptyRequestBody
  ): FutureResponse = exec(route, mediaType, params, _.post(body))

  protected def putCall(
    route:     String,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty,
    body:      RequestBody         = emptyRequestBody
  ): FutureResponse = exec(route, mediaType, params, _.put(body))

  protected def patchCall(
    route:     String,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty,
    body:      RequestBody         = emptyRequestBody
  ): FutureResponse = exec(route, mediaType, params, _.patch(body))

  protected def deleteCall(
    route:     String,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty,
    body:      RequestBody         = emptyRequestBody
  ): FutureResponse = exec(route, mediaType, params, _.delete(body))

  ///////////////////////
  // RESPONSE HANDLING //
  ///////////////////////

  protected def isSuccessful(future: FutureResponse)(implicit ec: EC): Future[Boolean] =
    future.map(_.isSuccessful)

  protected def json[T: Decoder](future: FutureResponse, errorMsg: String)(implicit ec: EC): Future[T] =
    future.flatMap {
      _.as[T].fold(
        code ⇒ Future.failed(new RuntimeException(s"$errorMsg, status code: $code")),
        resp ⇒ Future.successful(resp.decoded)
      )
    }

  protected def jsonOptionalIfFailed[T: Decoder](future: FutureResponse, errorMsg: String)(implicit ec: EC): Future[Option[T]] =
    future.map {
      _.as[T].fold(
        code ⇒ failedRequestWithDefaultValue(errorMsg, code, None),
        _.decoded.some
      )
    }

  protected def raw(future: FutureResponse)(implicit ec: EC): Future[String] =
    future.map(_.raw)

  protected def rawOptional(future: FutureResponse, errorMsg: String)(implicit ec: EC): Future[Option[String]] =
    future.map {
      _.rawIfExists.fold(
        code ⇒ failedRequestWithDefaultValue(errorMsg, code, None),
        _.some
      )
    }

  protected def failedRequestWithDefaultValue[T](errorMessage: String, statusCode: Int, defaultValue: T) = {
    logger.error(s"$errorMessage, status code: $statusCode")
    defaultValue
  }

  //////////////////////
  // PAGINATION (GET) //
  //////////////////////

  protected def singlePageGetCall(
    route:     String,
    page:      Int,
    mediaType: String              = DefaultMediaType,
    params:    Map[String, String] = Map.empty
  ): FutureResponse = {
    require(page > 0, "Page numbers are 1-based (https://developer.github.com/v3/#pagination)")
    val pageParams = Map("page" → page.toString, "per_page" → config.itemsPerPage.toString)
    getCall(route, mediaType, params ++ pageParams)
  }

  protected def getAllPages[T: Decoder](
    route:        String,
    errorMessage: String,
    mediaType:    String              = DefaultMediaType,
    params:       Map[String, String] = Map.empty
  )(implicit ec: EC) = {
    def findNextPageUrl(linkHeader: String): Option[String] =
      linkHeader.split(",")
        .collectFirst { case PagesNavRegex(link, rel) if rel == "next" ⇒ link }

    def fetchAux(
      current:        FutureResponse,
      alreadyFetched: Future[Option[List[T]]]
    )(implicit ec: EC): Future[Option[List[T]]] =
      current.flatMap { currentResponse ⇒
        val linkHeader = Option(currentResponse.response.headers().get("Link"))
        val decoded = json[Option[List[T]]](Future.successful(currentResponse), errorMessage)
        linkHeader.flatMap(findNextPageUrl)
          .map(next ⇒ fetchAux(getCall(next, mediaType, params), decoded |+| alreadyFetched))
          .getOrElse(alreadyFetched)
      }

    fetchAux(getCall(route, mediaType, params), Future.successful(Nil.some))
  }

}
