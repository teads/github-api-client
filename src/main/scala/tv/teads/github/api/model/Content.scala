package tv.teads.github.api.model

import io.circe._

trait ContentFormats {
  self: UserCodec with LinksContentFormats ⇒

  implicit lazy val contentDecoder = Decoder.instance { cursor ⇒
    for {
      contentType ← cursor.downField("type").as[String]
      encoding ← cursor.downField("encoding").as[String]
      size ← cursor.downField("size").as[Long]
      name ← cursor.downField("name").as[String]
      path ← cursor.downField("path").as[String]
      content ← cursor.downField("contents").as[String]
      sha ← cursor.downField("sha").as[String]
      url ← cursor.downField("url").as[String]
      gitUrl ← cursor.downField("git_url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      downloadUrl ← cursor.downField("download_url").as[String]
      links ← cursor.downField("_links").as[LinksContent]
    } yield Content(contentType, encoding, size, name, path, content, sha, url, gitUrl, htmlUrl, downloadUrl, links)
  }
}
case class Content(
  contentType: String,
  encoding:    String,
  size:        Long,
  name:        String,
  path:        String,
  content:     String,
  sha:         String,
  url:         String,
  gitUrl:      String,
  htmlUrl:     String,
  downloadUrl: String,
  links:       LinksContent
)
