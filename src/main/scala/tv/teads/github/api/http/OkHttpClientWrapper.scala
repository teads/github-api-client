package tv.teads.github.api.http

import java.io.{IOException, File}

import com.typesafe.scalalogging.LazyLogging
import okhttp3.OkHttpClient.Builder

import scala.concurrent.{Promise, Future}

import okhttp3._

private[api] class OkHttpClientWrapper(authenticator: Option[Authenticator], maxCacheSize: Long, cacheRoot: File) extends LazyLogging {

  private[this] val okHttpClient = {
    val builder = new Builder()
    if (maxCacheSize != 0) builder.cache(new Cache(cacheRoot, maxCacheSize))
    builder.build()
  }

  def executeAsync(requestBuilder: Request.Builder, customAuthenticator: Option[Authenticator]): Future[Response] = {
    val auth = customAuthenticator orElse authenticator
    val authenticatedRequest = auth.map(_(requestBuilder)).getOrElse(requestBuilder).build()
    logger.debug(s"${authenticatedRequest.method()} ${authenticatedRequest.url()}")
    val promise = Promise[Response]()
    okHttpClient.newCall(authenticatedRequest).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = promise.failure(e)
      override def onResponse(call: Call, response: Response): Unit = promise.success(response)
    })
    promise.future
  }

  def clearCache(): Unit = okHttpClient.cache().evictAll()
}
