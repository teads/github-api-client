package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait GHCommitFormats {
  self: UserFormats  with ParentFormats with CommitDetailFormats =>
  implicit lazy val  ghCommitJsonWrite : Write[GHCommit, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[GHCommit, JsObject]
  }

  implicit lazy val  ghCommitJsonRead = {
    import play.api.data.mapping.json.Rules._  // let's no leak implicits everywhere
    Rule.gen[JsValue, GHCommit]
  }

}

case class GHCommit(
                   sha: String,
                   commit: CommitDetail,
                   url: String,
                   html_url: String,
                   comments_url: String,
                   author: User,
                   committer: User,
                   parents: List[Parent]
                   )
