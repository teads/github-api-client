package tv.teads.github.api.models
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait IssueFormats {
  self :UserFormats with LabelFormats =>
  implicit lazy val  issueJsonWrite : Write[Issue, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Issue, JsObject]
  }

  implicit lazy val  issueJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Issue]
  }

}
case class Issue(
                  url: String,
                  labels_url: String,
                  comments_url: String,
                  events_url: String,
                  html_url: String,
                  id: Long,
                  number: Long,
                  title: String,
                  user: User,
                  labels: List[Label],
                  state: String,
                  locked: Boolean,
                  assignee: Option[User],
                  milestone: Option[String],
                  comments: Long,
                  created_at: String,
                  updated_at: String,
                  closed_at: Option[String],
                  body: String
                  )
