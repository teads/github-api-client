package tv.teads.github.api.models

import play.api.data.mapping._
import play.api.libs.json.{JsObject, JsValue}

trait CommitDetailFormats {
  self: UserFormats with AuthorFormats with TreeFormats ⇒

  implicit lazy val commitDetailJsonRead = From[JsValue] { __ ⇒
    import play.api.data.mapping.json.Rules._
    // let's no leak implicits everywhere
    (
      (__ \ "author").read[Author] ~
      (__ \ "committer").read[Author] ~
      (__ \ "message").read[String] ~
      (__ \ "tree").read[Tree] ~
      (__ \ "url").read[String] ~
      (__ \ "comment_count").read[Long]
    )(CommitDetail.apply _)
  }
}
case class CommitDetail(
  author:       Author,
  committer:    Author,
  message:      String,
  tree:         Tree,
  url:          String,
  commentCount: Long
)
