package tv.teads.github.api.model

import io.circe._

trait CommitDetailCodec {
  self: UserCodec with AuthorCodec with TreeCodec ⇒

  implicit lazy val commitDetailDecoder = Decoder.instance { cursor ⇒
    for {
      author ← cursor.downField("author").as[Author]
      committer ← cursor.downField("committer").as[Author]
      message ← cursor.downField("message").as[String]
      tree ← cursor.downField("tree").as[Tree]
      url ← cursor.downField("url").as[String]
      commentCount ← cursor.downField("comment_count").as[Long]
    } yield CommitDetail(author, committer, message, tree, url, commentCount)
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
