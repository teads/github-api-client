package tv.teads.github.api.http

import com.squareup.okhttp.Request

object Authenticator {
  def fromFunction(f: Request.Builder ⇒ Request.Builder): Authenticator =
    new Authenticator { override def apply(v1: Request.Builder) = f(v1) }
}
trait Authenticator extends (Request.Builder ⇒ Request.Builder)
