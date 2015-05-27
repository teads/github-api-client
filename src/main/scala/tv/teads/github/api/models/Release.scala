package tv.teads.github.api.models

import play.api.libs.json.{JsObject, JsValue}
import play.api.data.mapping._

trait ReleaseFormats {
  self :UserFormats with AuthorFormats =>
  implicit lazy val  releaseJsonWrite : Write[Release, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[Release, JsObject]
  }

  implicit lazy val  releaseJsonRead = {
    import play.api.data.mapping.json.Rules._ // let's no leak implicits everywhere
    Rule.gen[JsValue, Release]
  }

}
case class Release(
                    url: String,
                    assets_url: String,
                    upload_url: String,
                    html_url: String,
                    id: Long,
                    tag_name: String,
                    target_commitish: String,
                    name: String,
                    draft: Boolean,
                    author: Author,
                    prerelease: Boolean,
                    created_at: String,
                    published_at: String,
                    assets: List[String],
                    tarball_url: String,
                    zipball_url: String,
                    body: String
                    )
