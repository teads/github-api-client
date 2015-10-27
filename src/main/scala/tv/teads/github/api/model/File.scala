package tv.teads.github.api.model

import io.circe._

trait FileCodec {
  implicit lazy val fileDecoder = Decoder.instance { cursor ⇒
    for {
      sha ← cursor.downField("sha").as[String]
      filename ← cursor.downField("filename").as[String]
      status ← cursor.downField("status").as[String]
      additions ← cursor.downField("additions").as[Long]
      deletions ← cursor.downField("deletions").as[Long]
      changes ← cursor.downField("changes").as[Long]
      blobUrl ← cursor.downField("blob_url").as[String]
      rawUrl ← cursor.downField("raw_url").as[String]
      contentsUrl ← cursor.downField("contents_url").as[String]
      patch ← cursor.downField("patch").as[String]
    } yield File(sha, filename, status, additions, deletions, changes, blobUrl, rawUrl, contentsUrl, patch)
  }
}

case class File(
  sha:         String,
  filename:    String,
  status:      String,
  additions:   Long,
  deletions:   Long,
  changes:     Long,
  blobUrl:     String,
  rawUrl:      String,
  contentsUrl: String,
  patch:       String
)
