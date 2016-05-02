package tv.teads.github.api.util

import enumeratum.EnumEntry
import shapeless.ops.hlist.ToTraversable
import shapeless.record._
import shapeless.ops.record._
import shapeless._

private[api] object ClassToMap {

  implicit class ClassToMapOps[A <: Product](val a: A) extends AnyVal {

    def toStringMap[L <: HList, KS <: HList, VS <: HList](
      implicit
      gen:    LabelledGeneric.Aux[A, L],
      keys:   Keys.Aux[L, KS],
      values: Values.Aux[L, VS],
      ts1:    ToTraversable.Aux[KS, List, Symbol],
      ts2:    ToTraversable.Aux[VS, List, Any]
    ): Map[String, String] = {
      val r = gen.to(a)
      val anyMap = r.keys.to[List].map(_.name).zip(r.values.to[List]).toMap
      mapValuesTtoIterables(anyMap)
        .collect { case (k, v) if v.nonEmpty ⇒ k → v.map(printValue).mkString(",") }
    }

    private def mapValuesTtoIterables[K, V](map: Map[K, V]) = {
      map.mapValues {
        case it: Iterable[_] ⇒ it
        case opt: Option[_]  ⇒ Option.option2Iterable(opt)
        case _               ⇒ throw new IllegalArgumentException("toStringMap only support classes with Options or List members")
      }

    }

    private def printValue[T](v: T) = v match {
      case enum: EnumEntry ⇒ enum.entryName
      case other           ⇒ other.toString
    }

  }
}
