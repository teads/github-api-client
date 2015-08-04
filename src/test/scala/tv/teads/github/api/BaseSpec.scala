package tv.teads.github.api

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers, OptionValues }

abstract class BaseSpec
    extends TestKit(ActorSystem("github-client-test"))
    with FlatSpecLike
    with BeforeAndAfterAll
    with Matchers
    with OptionValues
    with ScalaFutures {

  def loadFile(file: String) = scala.io.Source.fromURL(getClass.getClassLoader.getResource(file)).mkString

  override protected def afterAll(): Unit = system.shutdown()
}
