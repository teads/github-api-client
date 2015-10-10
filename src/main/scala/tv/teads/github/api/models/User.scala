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
  implicit lazy val userJsonWrite: Write[User, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[User, JsObject]
  }

  implicit lazy val userJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "name").read[Option[String]] ~
      (__ \ "email").read[Option[String]] ~
      (__ \ "login").read[Option[String]] ~
      (__ \ "id").read[Option[Long]] ~
      (__ \ "avatar_url").read[Option[String]] ~
      (__ \ "gravatar_id").read[Option[String]] ~
      (__ \ "url").read[Option[String]] ~
      (__ \ "html_url").read[Option[String]] ~
      (__ \ "followers_url").read[Option[String]] ~
      (__ \ "following_url").read[Option[String]] ~
      (__ \ "gists_url").read[Option[String]] ~
      (__ \ "starred_url").read[Option[String]] ~
      (__ \ "subscriptions_url").read[Option[String]] ~
      (__ \ "organizations_url").read[Option[String]] ~
      (__ \ "repos_url").read[Option[String]] ~
      (__ \ "events_url").read[Option[String]] ~
      (__ \ "receivend_events_url").read[Option[String]] ~
      (__ \ "type").read[Option[String]] ~
      (__ \ "site_admin").read[Option[Boolean]]
    )(User.apply _)
  }

}

case class User(
  name:              Option[String],
  email:             Option[String],
  login:             Option[String],
  id:                Option[Long],
  avatarUrl:         Option[String],
  gravatar_id:       Option[String],
  url:               Option[String],
  htmlUrl:           Option[String],
  followersUrl:      Option[String],
  followingUrl:      Option[String],
  gistsUrl:          Option[String],
  starredUrl:        Option[String],
  subscriptionsUrl:  Option[String],
  organizationsUrl:  Option[String],
  reposUrl:          Option[String],
  eventsUrl:         Option[String],
  receivedEventsUrl: Option[String],
  typ:               Option[String],
  siteAdmin:         Option[Boolean]
)
//extends Owner
