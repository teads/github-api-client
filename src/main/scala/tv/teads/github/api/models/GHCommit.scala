package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait GHCommitFormats {
  self: UserFormats with ParentFormats with CommitDetailFormats ⇒
  implicit lazy val ghCommitJsonWrite: Write[GHCommit, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[GHCommit, JsObject]
  }

  implicit lazy val ghCommitJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "sha").read[String] ~
      (__ \ "commit").read[CommitDetail] ~
      (__ \ "url").read[String] ~
      (__ \ "html_url").read[String] ~
      (__ \ "comments_url").read[String] ~
      (__ \ "author").read[User] ~
      (__ \ "committer").read[User] ~
      (__ \ "parents").read[List[Parent]]
    )(GHCommit.apply _)
  }
}

case class GHCommit(
  sha:         String,
  commit:      CommitDetail,
  url:         String,
  htmlUrl:     String,
  commentsUrl: String,
  author:      User,
  committer:   User,
  parents:     List[Parent]
)
