package tv.teads.github.api

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, OptionValues}

abstract class BaseSpec
  extends FlatSpec
  with BeforeAndAfterAll
  with Matchers
  with OptionValues
  with ScalaFutures
   {

  def loadFile(file:String) = scala.io.Source.fromURL(getClass.getClassLoader.getResource(file)).mkString
}
