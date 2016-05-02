package tv.teads.github.api.services

import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{Matchers, OptionValues, WordSpec}
import tv.teads.github.api.{AbstractSpec, GithubApiClientBuilder}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

abstract class AbstractServiceSpec
    extends AbstractSpec
    with ScalaFutures
    with Eventually {

  implicit final val ec = ExecutionContext.global

  val teadsClient = GithubApiClientBuilder().disableCache.apiToken(sys.env("GITHUB_TOKEN")).owner("teads").build

  override implicit final val patienceConfig = PatienceConfig(10.seconds, 1.second)
}
