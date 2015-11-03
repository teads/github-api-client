package tv.teads.github.api.http

private[api] case class Credentials(
  username:          String,
  password:          String,
  twoFactorAuthCode: Option[String]
)