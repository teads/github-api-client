package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe._

import tv.teads.github.api.json.ZonedDateTimeCodec

trait ReleaseCodec {
  self: UserCodec with AuthorCodec with ZonedDateTimeCodec ⇒

  implicit lazy val releaseDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      assetsUrl ← cursor.downField("assets_url").as[String]
      uploadUrl ← cursor.downField("upload_url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      id ← cursor.downField("id").as[Long]
      tagName ← cursor.downField("tag_name").as[String]
      targetComitish ← cursor.downField("target_commitish").as[String]
      name ← cursor.downField("name").as[String]
      draft ← cursor.downField("draft").as[Boolean]
      author ← cursor.downField("author").as[Author]
      prerelease ← cursor.downField("prerelease").as[Boolean]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      publishedAt ← cursor.downField("published_at").as[ZonedDateTime]
      assets ← cursor.downField("assets").as[List[String]]
      tarballUrl ← cursor.downField("tarball_url").as[String]
      zipballUrl ← cursor.downField("zipball_url").as[String]
      body ← cursor.downField("body").as[String]
    } yield Release(
      url, assetsUrl, uploadUrl, htmlUrl, id, tagName, targetComitish, name, draft, author,
      prerelease, createdAt, publishedAt, assets, tarballUrl, zipballUrl, body
    )
  }

}
case class Release(
  url:             String,
  assetsUrl:       String,
  uploadUrl:       String,
  htmlUrl:         String,
  id:              Long,
  tagName:         String,
  targetCommitish: String,
  name:            String,
  draft:           Boolean,
  author:          Author,
  prerelease:      Boolean,
  createdAt:       ZonedDateTime,
  publishedAt:     ZonedDateTime,
  assets:          List[String],
  tarballUrl:      String,
  zipballUrl:      String,
  body:            String
)
