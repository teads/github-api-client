package tv.teads.github.api.util

import shapeless.ops.hlist.ToTraversable
import shapeless.record._
import shapeless.ops.record._
import shapeless._

object CaseClassToMap {

  implicit class ToMapOps[A <: Product](val a: A) extends AnyVal {

    def toMapStringified[L <: HList, KS <: HList, VS <: HList](
      implicit
      gen:    LabelledGeneric.Aux[A, L],
      keys:   Keys.Aux[L, KS],
      values: Values.Aux[L, VS],
      ts1:    ToTraversable.Aux[KS, List, Symbol],
      ts2:    ToTraversable.Aux[VS, List, Any]
    ): Map[String, String] = {
      val r = gen.to(a)
      val anyMap = r.keys.to[List].map(_.name).zip(r.values.to[List]).toMap
      anyMap.collect {
        case (k, v) ⇒
          v match {
            case it: Iterable[_] ⇒ k → it
            case opt: Option[_]  ⇒ k → (opt: Iterable[_])
          }
      }.collect { case (k, v) if v.nonEmpty ⇒ k → v.mkString(",") }
    }

  }
}
