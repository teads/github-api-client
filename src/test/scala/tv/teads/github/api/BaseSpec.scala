package tv.teads.github.api

import org.scalatest.concurrent.ScalaFutures
import org.scalatest._
import scala.concurrent.duration._

abstract class BaseSpec
    extends FlatSpecLike
    with Matchers
    with OptionValues
    with ScalaFutures {

  val ebuzzingClient = GithubApiClientBuilder().apiToken(sys.env("GITHUB_TOKEN")).owner("ebuzzing").build

  override implicit val patienceConfig = PatienceConfig(30.seconds, 1.second)

  def loadFile(file: String) = scala.io.Source.fromURL(getClass.getClassLoader.getResource(file)).mkString

}
