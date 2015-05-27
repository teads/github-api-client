package tv.teads.github.api.models
import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait HeadFormats  {
  self :UserFormats with RepositoryFormats =>
  implicit lazy val  headJsonWrite: Write[Head, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Head, JsObject]
  }

  implicit lazy val  headJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Head]
  }
}

case class Head(
                 label: String,
                 ref: String,
                 sha: String,
                 user: User,
                 repo: Option[Repository]
                 )

trait SelfFormats  {
  implicit lazy val  selfJsonWrite: Write[Self, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Self, JsObject]
  }

  implicit lazy val  selfJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Self]
  }
}

case class Self(
                 href: String
                 )

trait LinkFormats  {

  self :UserFormats with SelfFormats =>
  implicit lazy val  linkJsonWrite: Write[Link, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Link, JsObject]
  }

  implicit lazy val  linkJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Link]
  }
}

case class Link(
                 self: Self,
                 html: Self,
                 issue: Self,
                 comments: Self,
                 review_comments: Self,
                 review_comment: Self,
                 commits: Self,
                 statuses: Self
                 )

trait PullRequestLinksFormats  {

  implicit lazy val  pullRequestLinksJsonWrite: Write[PullRequestLinks, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[PullRequestLinks, JsObject]
  }

  implicit lazy val  pullRequestLinksJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, PullRequestLinks]
  }
}

case class PullRequestLinks(
                 self: String,
                 html: String,
                 issue: String,
                 comments: String,
                 review_comments: String,
                 review_comment: String,
                 commits: String,
                 statuses: String
                 )

trait LinkPullRequestFormats  {

  self :UserFormats with SelfFormats =>
  implicit lazy val  linkPullRequestJsonWrite: Write[LinkPullRequest, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[LinkPullRequest, JsObject]
  }

  implicit lazy val  linkPullRequestJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, LinkPullRequest]
  }
}
case class LinkPullRequest(
                            self: Self,
                            html: Self,
                            pull_request: Self
                            )


trait LinkContentFormats  {
  implicit lazy val  linkContentJsonWrite: Write[LinkContent, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[LinkContent, JsObject]
  }

  implicit lazy val  linkContentJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, LinkContent]
  }
}
case class LinkContent(
                          self: String,
                          html: String,
                          git: String
                          )

trait TreeFormats  {
  implicit lazy val  treeJsonWrite: Write[Tree, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Tree, JsObject]
  }

  implicit lazy val  treeJsonRead = {
    import play.api.data.mapping.json.Rules._
    Rule.gen[JsValue, Tree]
  }
}
case class Tree(
                   sha: String,
                   url: String
                   )
