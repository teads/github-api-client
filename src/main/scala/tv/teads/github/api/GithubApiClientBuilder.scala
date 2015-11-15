package tv.teads.github.api

import java.io.File

import scala.util.Properties

import tv.teads.github.api.http._
import tv.teads.github.api.http.Authenticators._

private[api] case class GithubApiClientConfigBuilder(
  owner:        Option[String]      = None,
  apiUrl:       String              = "https://api.github.com",
  apiToken:     Option[String]      = None,
  credentials:  Option[Credentials] = None,
  itemsPerPage: Int                 = 30,
  maxCacheSize: Long                = Long.MaxValue,
  cacheRoot:    File                = new File(Properties.tmpDir, "github-api-client")
)

object GithubApiClientBuilder {
  def apply() = new GithubApiClientBuilder(GithubApiClientConfigBuilder())
}
class GithubApiClientBuilder private[api] (builder: GithubApiClientConfigBuilder) {

  def owner(owner: String) =
    new GithubApiClientBuilder(builder.copy(owner = Some(owner)))

  def apiUrl(apiUrl: String) =
    new GithubApiClientBuilder(builder.copy(apiUrl = apiUrl))

  def apiToken(token: String) =
    new GithubApiClientBuilder(builder.copy(apiToken = Some(token)))

  def itemsPerPage(itemsPerPage: Int) =
    new GithubApiClientBuilder(builder.copy(itemsPerPage = itemsPerPage))

  def userCredentials(username: String, password: String, twoFactorAuthCode: Option[String] = None) =
    new GithubApiClientBuilder(builder.copy(credentials = Some(Credentials(username, password, twoFactorAuthCode))))

  def maxCacheSize(maxCacheSize: Long) =
    new GithubApiClientBuilder(builder.copy(maxCacheSize = maxCacheSize))

  def build: GithubApiClient = {
    if (builder.owner.isEmpty) throw new IllegalStateException("User/Organization has not been configured.")
    val authenticator = builder.apiToken.map(apiTokenAuthenticator) orElse builder.credentials.map(credentialsAuthenticator)

    val config = GithubApiClientConfig(builder.owner.get, builder.apiUrl, authenticator, builder.itemsPerPage, builder.maxCacheSize, builder.cacheRoot)
    GithubApiClient(config)
  }
}
