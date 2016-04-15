package tv.teads.github.api

import java.io.File

import tv.teads.github.api.http.{OkHttpClientWrapper, Authenticator}

private[api] case class GithubApiClientConfig(
    owner:         String,
    apiUrl:        String,
    authenticator: Option[Authenticator],
    itemsPerPage:  Int,
    maxCacheSize:  Long,
    cacheRoot:     File
) {
  val client = new OkHttpClientWrapper(authenticator, maxCacheSize, cacheRoot)
}

case class GithubApiClient private[api] (config: GithubApiClientConfig) {
  def clearCache(): Unit = config.client.clearCache()
}
