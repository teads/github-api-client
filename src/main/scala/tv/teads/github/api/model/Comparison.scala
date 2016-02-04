package tv.teads.github.api.model

import io.circe._

trait ComparisonCodec {
  self: GHCommitCodec with UserCodec with ParentCodec with CommitDetailCodec with FileCodec ⇒

  implicit lazy val comparisonDecoder = Decoder.instance { cursor ⇒
    for {
      url ← cursor.downField("url").as[String]
      htmlUrl ← cursor.downField("html_url").as[String]
      permalinkUrl ← cursor.downField("permalink_url").as[String]
      diffUrl ← cursor.downField("diff_url").as[String]
      patchUrl ← cursor.downField("patch_url").as[String]
      baseCommit ← cursor.downField("base_commit").as[GHCommit]
      mergeBaseCommit ← cursor.downField("merge_base_commit").as[GHCommit]
      status ← cursor.downField("status").as[String]
      aheadBy ← cursor.downField("ahead_by").as[Long]
      behindBy ← cursor.downField("behind_by").as[Long]
      totalCommits ← cursor.downField("total_commits").as[Long]
      commits ← cursor.downField("commits").as[List[GHCommit]]
      files ← cursor.downField("files").as[List[File]]
    } yield Comparison(url, htmlUrl, permalinkUrl, diffUrl, patchUrl, baseCommit, mergeBaseCommit,
      status, aheadBy, behindBy, totalCommits, commits, files)
  }
}

case class Comparison(
  url:             String,
  htmlUrl:         String,
  permalinkUrl:    String,
  diffUrl:         String,
  patchUrl:        String,
  baseCommit:      GHCommit,
  mergeBaseCommit: GHCommit,
  status:          String,
  aheadBy:         Long,
  behindBy:        Long,
  totalCommits:    Long,
  commits:         List[GHCommit],
  files:           List[File]
)
