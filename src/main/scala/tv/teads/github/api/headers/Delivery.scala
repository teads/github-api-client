package tv.teads.github.api.headers

object Delivery {
  lazy val GithubEvent = "X-Github-Event"
  lazy val HubSignature = "X-Hub-Signature"
  lazy val GithubDelivery = "X-Github-Delivery"

  object RateLimit {
    lazy val Limit = "X-RateLimit-Limit"
    lazy val Remaining = "X-RateLimit-Remaining"

  }
}
