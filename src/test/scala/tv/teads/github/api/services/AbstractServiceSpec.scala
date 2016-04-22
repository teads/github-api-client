package tv.teads.github.api.services

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{WordSpec, Matchers, OptionValues}
import tv.teads.github.api.GithubApiClientBuilder

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

abstract class AbstractServiceSpec
    extends WordSpec
    with Matchers
    with OptionValues
    with ScalaFutures {

  implicit val ec = ExecutionContext.global

  val teadsClient = GithubApiClientBuilder().disableCache.apiToken(sys.env("GITHUB_TOKEN")).owner("teads").build

  override implicit val patienceConfig = PatienceConfig(10.seconds, 1.second)
}
