package tv.teads.github.api.models
import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait ParentFormats {
  implicit lazy val parentJsonWrite: Write[Parent, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Parent, JsObject]
  }

  implicit lazy val parentJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "sha").read[String] ~
      (__ \ "url").read[String] ~
      (__ \ "html_url").read[String]
    )(Parent.apply _)
  }

}
case class Parent(
  sha:     String,
  url:     String,
  htmlUrl: String
)
