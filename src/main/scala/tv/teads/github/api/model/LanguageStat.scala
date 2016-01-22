package tv.teads.github.api.model

import io.circe.Decoder

trait LanguageStatCodec {
  implicit lazy val languageStatDecoder: Decoder[List[LanguageStat]] = Decoder.instance { cursor ⇒
    cursor.as[Map[String, Long]].map(_.map(LanguageStat.tupled).toList)
  }

}

case class LanguageStat(
  name:  String,
  lines: Long
)
