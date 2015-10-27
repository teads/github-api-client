package tv.teads.github.api.model

import io.circe._

trait GHCommitCodec {
  self: UserCodec with ParentCodec with CommitDetailCodec ⇒

  implicit lazy val ghCommitDecoder = Decoder.instance { cursor ⇒
    for {
      sha ← cursor.downField("sha").as[String]
      commit ← cursor.downField("commit").as[CommitDetail]
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      commentsUrl ← cursor.downField("comments_url").as[String]
      author ← cursor.downField("author").as[User]
      committer ← cursor.downField("committer").as[User]
      parents ← cursor.downField("parents").as[List[Parent]]
    } yield GHCommit(sha, commit, url, htmlUrl, commentsUrl, author, committer, parents)
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
