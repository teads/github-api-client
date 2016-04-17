package tv.teads.github.api.http

import okhttp3.Request

private[api] object Authenticator {
  def fromFunction(f: Request.Builder ⇒ Request.Builder): Authenticator =
    new Authenticator { override def apply(v1: Request.Builder) = f(v1) }
}
private[api] trait Authenticator extends (Request.Builder ⇒ Request.Builder)
