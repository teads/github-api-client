package tv.teads.github.api.util

import java.io.Closeable

import scala.util.control.NonFatal

private[api] object IO {
  def withCloseable[T <: Closeable, U](closeable: T)(f: T ⇒ U): U =
    try f(closeable) catch { case NonFatal(e) ⇒ throw e } finally closeable.close()
}
