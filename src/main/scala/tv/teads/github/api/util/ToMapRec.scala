package tv.teads.github.api.util

import shapeless._, labelled.FieldType

// http://stackoverflow.com/questions/31633563/converting-nested-case-classes-to-nested-maps-using-shapeless
trait LowPriorityToMapRec {
  implicit def hconsToMapRec1[K <: Symbol, V, T <: HList](
    implicit
    wit: Witness.Aux[K], tmrT: ToMapRec[T]
  ): ToMapRec[::[FieldType[K, V], T]] = new ToMapRec[FieldType[K, V] :: T] {
    def apply(l: FieldType[K, V] :: T): Map[String, Any] =
      tmrT(l.tail) + (wit.value.name → l.head)
  }
}

trait ToMapRec[L <: HList] {
  def apply(l: L): Map[String, Any]
}

object ToMapRec extends LowPriorityToMapRec {
  implicit val hnilToMapRec: ToMapRec[HNil] = new ToMapRec[HNil] {
    def apply(l: HNil): Map[String, Any] = Map.empty
  }

  implicit def hconsToMapRec0[K <: Symbol, V, R <: HList, T <: HList](
    implicit
    wit: Witness.Aux[K],
    gen: LabelledGeneric.Aux[V, R], tmrH: ToMapRec[R], tmrT: ToMapRec[T]
  ): ToMapRec[FieldType[K, V] :: T] = new ToMapRec[FieldType[K, V] :: T] {
    def apply(l: FieldType[K, V] :: T): Map[String, Any] =
      tmrT(l.tail) + (wit.value.name → tmrH(gen.to(l.head)))
  }

  implicit class ToMapRecOps[A](val a: A) extends AnyVal {
    def toMapRec[L <: HList](
      implicit
      gen: LabelledGeneric.Aux[A, L],
      tmr: ToMapRec[L]
    ): Map[String, Any] = tmr(gen.to(a))

    def toMapRecStringified[L <: HList](
      implicit
      gen: LabelledGeneric.Aux[A, L],
      tmr: ToMapRec[L]
    ): Map[String, String] = {
      toMapRec.collect {
        case (k, v) ⇒
          v match {
            case it: Iterable[_] ⇒ k → it
            case opt: Option[_]  ⇒ k → (opt: Iterable[_])
          }
      }.collect { case (k, v) if v.nonEmpty ⇒ k → v.mkString(",") }
    }
  }
}