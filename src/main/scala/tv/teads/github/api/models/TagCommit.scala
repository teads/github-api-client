package tv.teads.github.api.models
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait TagCommitFormats {
  implicit lazy val tagCommitJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, TagCommit]
  }

}
case class TagCommit(
  sha: String,
  url: String
)
