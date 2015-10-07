package tv.teads.github.api.services

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

private[services] object Configuration {
  val configuration = ConfigFactory.load().as[Configuration]("github-api-client")
}
private[services] case class Configuration(url: String, token: String, paging: Int, organization: String) {
  val tokenHeader = "access_token" → token
  val paginationHeader = "per_page" → paging.toString
}
