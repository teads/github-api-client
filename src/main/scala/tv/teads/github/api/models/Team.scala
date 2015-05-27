package tv.teads.github.api.models
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait TeamFormats {
  implicit lazy val teamJsonWrite : Write[Team, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Team, JsObject]
  }

  implicit lazy val teamJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Team]
  }

}
case class Team(
                 name: String,
                 id: Long,
                 slug: String,
                 description: Option[String],
                 permission: Option[String],
                 url: String,
                 members_url: String,
                 repositories_url: String
                 )
