package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait LabelFormats {
  implicit lazy val  labelJsonWrite : Write[Label, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Label, JsObject]
  }

  implicit lazy val  labelJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Label]
  }

}


case class Label(
                   url: String,
                   name: String,
                   color: String
                   )
