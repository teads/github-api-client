package tv.teads.github.api.util

import cats.syntax.option._
import enumeratum._, enumeratum.EnumEntry.Lowercase
import tv.teads.github.api.AbstractSpec
import tv.teads.github.api.util.ClassToMap._

class ClassToMapSpec extends AbstractSpec {

  "toStringMap" should {
    "be able to convert a case class with Options to map, dropping Nones " in {
      case class Test(foo: Option[String], bar: Option[String])

      Test("test".some, None).toStringMap shouldBe Map("foo" → "test")
    }

    "be able to convert a case class with Iterables to map, dropping empty ones" in {
      case class Test(foo: List[String], bar: List[String])

      Test(List("test", "test2"), Nil).toStringMap shouldBe Map("foo" → "test,test2")
    }

    "be able to convert a case class with mixed Iterables and Options to map, dropping empty ones or Nones" in {
      case class Test(foo: List[String], bar: List[String], quz: Option[Int], qix: Option[Boolean])

      Test(List("test", "test2"), Nil, 1.some, None).toStringMap shouldBe Map("foo" → "test,test2", "quz" → "1")
    }

    "be able to convert EnumEntries to String using their entryName" in {
      sealed trait TestEnum extends EnumEntry with Lowercase
      object TestEnum extends Enum[TestEnum] {
        override def values: Seq[TestEnum] = findValues
        case object Foo extends TestEnum
      }

      case class Test(foo: List[String], bar: Option[TestEnum])

      Test(List("test"), TestEnum.Foo.some).toStringMap shouldBe Map("foo" → "test", "bar" → "foo")
    }

    "throw an exception if the class is not entirely composed of Iterables or Options" in {
      case class Test(foo: Option[String], bar: Int)

      an[IllegalArgumentException] should be thrownBy Test("test".some, 1).toStringMap
    }
  }

}
