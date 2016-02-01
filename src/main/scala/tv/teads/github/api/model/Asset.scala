package tv.teads.github.api.model

import java.time.ZonedDateTime

import io.circe.Decoder
import io.circe.generic.semiauto._
import tv.teads.github.api.json.ZonedDateTimeCodec

trait AssetCodec {
  self: UserCodec with ZonedDateTimeCodec ⇒

  implicit lazy val assetDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      browserDownloadUrl ← cursor.downField("browser_download_url").as[String]
      id ← cursor.downField("id").as[Long]
      name ← cursor.downField("name").as[String]
      label ← cursor.downField("label").as[Option[String]]
      state ← cursor.downField("state").as[String]
      contentType ← cursor.downField("content_type").as[String]
      size ← cursor.downField("size").as[Long]
      downloadCount ← cursor.downField("download_count").as[Long]
      createdAt ← cursor.downField("created_at").as[ZonedDateTime]
      updatedAt ← cursor.downField("updated_at").as[ZonedDateTime]
      uploader ← cursor.downField("uploader").as[User]
    } yield Asset(
      url, browserDownloadUrl, id, name, label, state, contentType, size,
      downloadCount, createdAt, updatedAt, uploader
    )
  }

}

case class Asset(
  url:                String,
  browserDownloadUrl: String,
  id:                 Long,
  name:               String,
  label:              Option[String],
  state:              String,
  contentType:        String,
  size:               Long,
  downloadCount:      Long,
  createdAt:          ZonedDateTime,
  updatedAt:          ZonedDateTime,
  uploader:           User
)
