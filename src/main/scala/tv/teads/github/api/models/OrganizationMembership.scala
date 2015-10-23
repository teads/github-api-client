package tv.teads.github.api.models

import play.api.data.mapping.From
import play.api.libs.json.JsValue

trait OrganizationMembershipFormats {
  implicit lazy val organizationMembership = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    import tv.teads.github.api.util.CustomRules._
    // let's no leak implicits everywhere
    (
      (__ \ "role").read[String] ~
      (__ \ "state").read[String]
    )(OrganizationMembership.apply _)
  }

}

case class OrganizationMembership(
  role:  String,
  state: String
)

