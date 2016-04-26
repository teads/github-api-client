package tv.teads.github.api.model.miscellaneous

import io.circe._

object LicenseFull {
  implicit final val LicenseFullDecoder =
    Decoder.forProduct11(
      "key", "name", "html_url", "featured", "description", "category",
      "implementation", "required", "permitted", "forbidden", "body"
    )(LicenseFull.apply)
}
case class LicenseFull(
  key:            String,
  name:           String,
  htmlUrl:        String,
  featured:       Boolean,
  description:    String,
  category:       Option[String],
  implementation: String,
  required:       List[String],
  permitted:      List[String],
  forbidden:      List[String],
  body:           String
)