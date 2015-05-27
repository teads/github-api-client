package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

//trait OwnerFormats {
//  val typeFailure = Failure(Seq(Path -> Seq(ValidationError("validation.unknownOwnerType"))))
//
//  implicit lazy val  ownerRule: Rule[JsValue, Owner] = From[JsValue] { __ =>
//    import play.api.data.mapping.json.Rules._
//
//    (__ \ "login").read[Option[String]].flatMap[Owner] {
//      case Some(s) => Rule.gen[JsValue, User].fmap(x => x)
//      case None => Rule.gen[JsValue, Organization].fmap(x => x)
//    }
//  }
//
//  implicit lazy val  ownerWrite: Write[Owner, JsValue] = {
//      case User =>  Write.gen[User, JsObject]
//      case Organization =>  Write.gen[Organization, JsObject]
//
//  }
////  }
//// implicit lazy val  ownerWrite: Write[Owner, JsValue] = To[JsValue] { __ =>
////    import play.api.data.mapping.json.Writes._
////
////    (__ \ "owner").write[Owner] {
////      case u:User => Write.gen[User, JsObject]
////      case o:Organization => Write.gen[Organization, JsObject]
////    }
////  }
//}
//sealed trait Owner
//
//trait OrganizationFormats {
//  implicit lazy val organizationJsonWrite : Write[Organization, JsValue] = {
//    import play.api.data.mapping.json.Writes._
//    Write.gen[Organization, JsObject]
//  }
//
//  implicit lazy val organizationJsonRead = {
//    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
//    Rule.gen[JsValue, Organization]
//  }
//
//}
//
//case class Organization(
//                           login: Option[String],
//                           name: Option[String],
//                           email: Option[String]
//                           ) extends Owner

trait UserFormats {
  implicit lazy val userJsonWrite : Write[User, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[User, JsObject]
  }

  implicit lazy val userJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, User]
  }

}

case class User(
                 name:Option[String],
                 email:Option[String],
                 login: Option[String],
                 id: Option[Long],
                 avatar_url: Option[String],
                 gravatar_id: Option[String],
                 url: Option[String],
                 html_url: Option[String],
                 followers_url: Option[String],
                 following_url: Option[String],
                 gists_url: Option[String],
                 starred_url: Option[String],
                 subscriptions_url: Option[String],
                 organizations_url: Option[String],
                 repos_url: Option[String],
                 events_url: Option[String],
                 received_events_url: Option[String],
                 `type`: Option[String],
                 site_admin: Option[Boolean]
                 )
  //extends Owner
