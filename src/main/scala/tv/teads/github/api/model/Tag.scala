package tv.teads.github.api.model

import io.circe._

trait TagCodec {
  self: TagCommitCodec ⇒

  implicit lazy val tagDecoder = Decoder.instance { cursor ⇒
    for {
      name ← cursor.downField("name").as[String]
      commit ← cursor.downField("commit").as[TagCommit]
      zipballUrl ← cursor.downField("zipball_url").as[Option[String]]
      tarballUrl ← cursor.downField("tarball_url").as[Option[String]]
    } yield Tag(name, commit, zipballUrl, tarballUrl)
  }
}
case class Tag(
  name:       String,
  commit:     TagCommit,
  zipballUrl: Option[String],
  tarballUrl: Option[String]
)
