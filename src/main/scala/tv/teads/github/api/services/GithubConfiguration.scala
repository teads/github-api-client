package tv.teads.github.api.services

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

private[services] object GithubConfiguration {
  val configuration = ConfigFactory.load("github").as[GithubConfiguration]("github")
}
private[services] case class ApiConfiguration(url: String, token: String, paging: Int) {
  val tokenHeader = "access_token" → token
  val paginationHeader = "per_page" → paging.toString
}
private[services] case class GithubConfiguration(api: ApiConfiguration, organization: String)
