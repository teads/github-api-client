package tv.teads.github.api

private[api] case class GithubApiClientConfigBuilder(
  owner:        Option[String] = None,
  apiUrl:       String         = "https://api.github.com",
  apiToken:     Option[String] = None,
  itemsPerPage: Int            = 30
)

object GithubApiClientBuilder {
  def apply() = new GithubApiClientBuilder(GithubApiClientConfigBuilder())
}
class GithubApiClientBuilder private[api] (builder: GithubApiClientConfigBuilder) {

  def owner(owner: String) =
    new GithubApiClientBuilder(builder = builder.copy(owner = Some(owner)))

  def apiUrl(apiUrl: String) =
    new GithubApiClientBuilder(builder = builder.copy(apiUrl = apiUrl))

  def apiToken(token: String) =
    new GithubApiClientBuilder(builder = builder.copy(apiToken = Some(token)))

  def itemsPerPage(itemsPerPage: Int) =
    new GithubApiClientBuilder(builder = builder.copy(itemsPerPage = itemsPerPage))

  def build: GithubApiClient = {
    if (builder.owner.isEmpty) {
      throw new IllegalStateException("User/Organization has not been configured.")
    }
    if (builder.apiToken.isEmpty) {
      throw new IllegalStateException("API token has not been configured.")
    }

    val config = GithubApiClientConfig(builder.owner.get, builder.apiUrl, builder.apiToken.get, builder.itemsPerPage)
    GithubApiClient(config)
  }
}
