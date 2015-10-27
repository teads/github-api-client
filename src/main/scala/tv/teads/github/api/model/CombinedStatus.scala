package tv.teads.github.api.model

import io.circe._

trait CombinedStatusCodec {
  self: StatusCodec ⇒

  implicit lazy val combinedStatusDecoder = Decoder.instance { cursor ⇒
    for {
      state ← cursor.downField("state").as[StatusState]
      sha ← cursor.downField("sha").as[String]
      totalCount ← cursor.downField("total_count").as[Long]
      statuses ← cursor.downField("statuses").as[List[Status]]
      commitUrl ← cursor.downField("commit_url").as[String]
      url ← cursor.downField("url").as[String]
    } yield CombinedStatus(state, sha, totalCount, statuses, commitUrl, url)
  }
}

case class CombinedStatus(
  state:      StatusState,
  sha:        String,
  totalCount: Long,
  statuses:   List[Status],
  commitUrl:  String,
  url:        String
)
