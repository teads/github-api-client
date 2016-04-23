package tv.teads.github.api.model

import enumeratum._

trait JsonEnum[T <: EnumEntry] extends CirceEnum[T] with Enum[T]