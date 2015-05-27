package tv.teads.github.api.headers

/**
 * Created by gbougeard on 27/05/15.
 */
object Delivery {
  lazy val GithubEvent = "X-Github-Event"
  lazy val HubSignature = "X-Hub-Signature"
  lazy val GithubDelivery = "X-Github-Delivery"

  object RateLimit {
    lazy val Limit = "X-RateLimit-Limit"
    lazy val Remaining = "X-RateLimit-Remaining"

  }
}
