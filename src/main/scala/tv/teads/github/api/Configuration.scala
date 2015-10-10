package tv.teads.github.api

import com.typesafe.config.ConfigFactory

private[api] object Configuration {
  private val Prefix = "github-api-client."

  val configuration = {
    val config = ConfigFactory.load()

    Configuration(
      config.getString(Prefix + "url"),
      config.getString(Prefix + "token"),
      config.getInt(Prefix + "paging"),
      config.getString(Prefix + "organization")
    )
  }
}
private[api] case class Configuration(url: String, token: String, paging: Int, organization: String) {
  val tokenHeader = "access_token" → token
  val paginationHeader = "per_page" → paging.toString
}
