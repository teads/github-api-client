package tv.teads.github.api.models

import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait AuthorFormats {
  implicit lazy val authorJsonWrite: Write[Author, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Author, JsObject]
  }

  implicit lazy val authorJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Author]
  }

}

case class Author(
  name:     String,
  email:    String,
  username: Option[String],
  date:     Option[String]
)
