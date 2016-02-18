package tv.teads.github.api.http

import okhttp3.{Credentials ⇒ OkHttpCredentials}

object Authenticators {

  def apiTokenAuthenticator(apiToken: String) =
    Authenticator.fromFunction(_.addHeader("Authorization", s"token $apiToken"))

  def credentialsAuthenticator(credentials: Credentials) = Authenticator.fromFunction { builder ⇒
    val basicCredentials = OkHttpCredentials.basic(credentials.username, credentials.password)
    val base = builder.addHeader("Authorization", basicCredentials)
    credentials.twoFactorAuthCode.map(code ⇒ base.addHeader("X-GitHub-OTP", code)).getOrElse(base)
  }
}
