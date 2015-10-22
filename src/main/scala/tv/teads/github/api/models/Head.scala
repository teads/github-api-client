package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait HeadFormats {
  self: UserFormats with RepositoryFormats ⇒

  implicit lazy val headJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Head]
  }
}

case class Head(
  label: String,
  ref:   String,
  sha:   String,
  user:  User,
  repo:  Option[Repository]
)

trait HRefFormats {
  implicit lazy val selfJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, HRef]
  }
}

case class HRef(href: String)

trait LinksFormats {
  self: UserFormats with HRefFormats ⇒

  implicit lazy val linkJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "self").read[HRef] ~
      (__ \ "html").read[HRef] ~
      (__ \ "issue").read[HRef] ~
      (__ \ "comments").read[HRef] ~
      (__ \ "review_comments").read[HRef] ~
      (__ \ "review_comment").read[HRef] ~
      (__ \ "commits").read[HRef] ~
      (__ \ "statuses").read[HRef]
    )(Links.apply _)
  }
}

case class Links(
  self:           HRef,
  html:           HRef,
  issue:          HRef,
  comments:       HRef,
  reviewComments: HRef,
  reviewComment:  HRef,
  commits:        HRef,
  statuses:       HRef
)

trait PullRequestReviewCommentLinksFormats {
  self: UserFormats with HRefFormats ⇒

  implicit lazy val linkPullRequestJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "self").read[HRef] ~
      (__ \ "html").read[HRef] ~
      (__ \ "pull_request").read[HRef]
    )(PullRequestReviewCommentLinks.apply _)
  }
}

case class PullRequestReviewCommentLinks(
  self:        HRef,
  html:        HRef,
  pullRequest: HRef
)

trait LinksContentFormats {
  implicit lazy val linkContentJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, LinksContent]
  }
}

case class LinksContent(
  self: String,
  html: String,
  git:  String
)

trait TreeFormats {
  implicit lazy val treeJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Tree]
  }
}

case class Tree(
  sha: String,
  url: String
)
