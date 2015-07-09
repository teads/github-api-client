package tv.teads.github.api.models

import play.api.libs.json.{ JsObject, JsValue }
import play.api.data.mapping._

trait BranchFormats {
  self: UserFormats with TreeFormats ⇒
  implicit lazy val branchJsonWrite: Write[Branch, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Branch, JsObject]
  }

  implicit lazy val branchrJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Branch]
  }

}
case class Branch(
  name:   String,
  commit: Tree
)
