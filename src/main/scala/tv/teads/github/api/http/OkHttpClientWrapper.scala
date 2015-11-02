package tv.teads.github.api.http

import java.io.{IOException, File}

import scala.concurrent.{Promise, Future}

import com.squareup.okhttp._

private[api] class OkHttpClientWrapper(authenticator: Option[Authenticator], maxCacheSize: Long, cacheRoot: File) {

  private[this] val okHttpClient = {
    val client = new OkHttpClient()
    if (maxCacheSize != 0) client.setCache(new Cache(cacheRoot, maxCacheSize)) else client
  }

  def executeAsync(requestBuilder: Request.Builder, customAuthenticator: Option[Authenticator]): Future[Response] = {
    val auth = authenticator orElse customAuthenticator
    val authenticatedRequest = auth.map(_(requestBuilder)).getOrElse(requestBuilder).build()
    val promise = Promise[Response]()
    okHttpClient.newCall(authenticatedRequest).enqueue(new Callback {
      override def onFailure(request: Request, e: IOException): Unit = promise.failure(e)
      override def onResponse(response: Response): Unit = promise.success(response)
    })
    promise.future
  }
}
