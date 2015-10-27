package tv.teads.github.api.model

import io.circe._, io.circe.syntax._

trait TeamCodec {
  implicit lazy val teamEncoder = Encoder.instance[Team] { team ⇒
    import team._
    Json.obj(
      "name" → name.asJson,
      "id" → id.asJson,
      "slug" → slug.asJson,
      "privacy" → privacy.asJson,
      "description" → description.asJson,
      "permission" → permission.asJson,
      "url" → url.asJson,
      "members_url" → membersUrl.asJson,
      "repositories_url" → repositoriesUrl.asJson
    )
  }

  implicit lazy val teamDecoder = Decoder.instance { cursor ⇒
    for {
      name ← cursor.downField("name").as[String]
      id ← cursor.downField("id").as[Long]
      slug ← cursor.downField("slug").as[String]
      privacy ← cursor.downField("privacy").as[Option[Privacy]]
      description ← cursor.downField("description").as[Option[String]]
      permission ← cursor.downField("permission").as[Permission]
      url ← cursor.downField("url").as[String]
      membersUrl ← cursor.downField("members_url").as[String]
      repositoriesUrl ← cursor.downField("repositories_url").as[String]
    } yield Team(name, id, slug, privacy, description, permission, url, membersUrl, repositoriesUrl)
  }
}

case class Team(
  name:            String,
  id:              Long,
  slug:            String,
  privacy:         Option[Privacy],
  description:     Option[String],
  permission:      Permission,
  url:             String,
  membersUrl:      String,
  repositoriesUrl: String
)
