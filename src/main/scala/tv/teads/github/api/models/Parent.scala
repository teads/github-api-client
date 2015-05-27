package tv.teads.github.api.models
import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait ParentFormats {
  implicit lazy val  parentJsonWrite : Write[Parent, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Parent, JsObject]
  }

  implicit lazy val  parentJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Parent]
  }

}
case class Parent(
                    sha: String,
                    url: String,
                    html_url: String
                    )
