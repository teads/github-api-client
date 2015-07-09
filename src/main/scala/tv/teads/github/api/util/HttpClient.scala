package tv.teads.github.api.util

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem

import scala.collection.concurrent
import scala.collection.JavaConversions._
import scala.concurrent.{ ExecutionContext, Future }

import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling.FromResponseUnmarshaller

import scala.util.Try

sealed trait ClientResponse[T]
case class SuccessfulRequest[T](response: T, rawResponse: HttpResponse) extends ClientResponse[T]
case class FailedRequest[T](statusCode: StatusCode) extends ClientResponse[T]

object HttpClient {
  def apply(req: HttpRequest): HttpClient = HttpClient(req, Nil)

  private case class CacheKey(uri: Uri, method: HttpMethod)
  private case class CacheEntry(eTag: String, response: HttpResponse)

  private val responseCache: concurrent.Map[CacheKey, CacheEntry] = new ConcurrentHashMap[CacheKey, CacheEntry]

  def cleanCache() = responseCache.clear()
}
case class HttpClient private (
    req:          HttpRequest,
    sendPipeline: List[RequestTransformer]
) {

  implicit val actorSystem = ActorSystem("github-api-client")

  import HttpClient._

  implicit def dispatcher: ExecutionContext = actorSystem.dispatcher

  def withHeader(headerName: String, headerValue: String) =
    copy(sendPipeline = addHeader(headerName, headerValue) :: sendPipeline)

  def withBasicAuth(username: String, password: String) =
    copy(sendPipeline = addCredentials(BasicHttpCredentials(username, password)) :: sendPipeline)

  def executeRequest(): Future[HttpResponse] = {
    val cacheKey = CacheKey(req.uri, req.method)
    val cacheEnabledPipeline = addETagIfPossible(cacheKey)
    val pipeline = if (cacheEnabledPipeline.isEmpty) sendReceive else cacheEnabledPipeline.reduce(_ ~> _) ~> sendReceive
    pipeline(req).map {
      case response if response.status == StatusCodes.NotModified ⇒ // In not modified, return the cached response
        responseCache(cacheKey).response
      case response ⇒ // otherwise, save the eTag and the response if it has one
        response.headers.find(_.name == HttpHeaders.ETag.name).map(_.value).foreach { eTag ⇒
          responseCache(cacheKey) = CacheEntry(eTag, response)
        }
        response
    }
  }

  def executeRequestInto[T]()(implicit evidence: FromResponseUnmarshaller[T]) =
    executeRequest().map {
      case response if response.status.isSuccess ⇒
        Try(unmarshal(evidence)(response))
          .map(converted ⇒ SuccessfulRequest(converted, response))
          .getOrElse(FailedRequest(response.status))

      case response ⇒
        FailedRequest(response.status)
    }

  private def addETagIfPossible(cacheKey: CacheKey): List[RequestTransformer] = {
    if (responseCache.contains(cacheKey))
      addHeader(HttpHeaders.`If-None-Match`.name, responseCache(cacheKey).eTag) :: sendPipeline
    else sendPipeline
  }
}
