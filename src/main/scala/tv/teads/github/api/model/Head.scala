package tv.teads.github.api.model

import io.circe._, io.circe.generic.semiauto._

trait HeadCodec {
  self: UserCodec with RepositoryCodec ⇒

  implicit lazy val headDecoder = deriveDecoder[Head]
}

case class Head(
  label: String,
  ref:   String,
  sha:   String,
  user:  User,
  repo:  Option[Repository]
)

trait LinksCodec {
  self: UserCodec ⇒

  implicit lazy val linksDecoder = Decoder.instance { cursor ⇒
    for {
      self ← cursor.downField("self").downField("href").as[String]
      html ← cursor.downField("html").downField("href").as[String]
      issue ← cursor.downField("issue").downField("href").as[String]
      comments ← cursor.downField("comments").downField("href").as[String]
      reviewComments ← cursor.downField("review_comments").downField("href").as[String]
      reviewComment ← cursor.downField("review_comment").downField("href").as[String]
      commits ← cursor.downField("commits").downField("href").as[String]
      statuses ← cursor.downField("statuses").downField("href").as[String]
    } yield Links(self, html, issue, comments, reviewComments, reviewComment, commits, statuses)
  }
}

case class Links(
  self:           String,
  html:           String,
  issue:          String,
  comments:       String,
  reviewComments: String,
  reviewComment:  String,
  commits:        String,
  statuses:       String
)

trait PullRequestReviewCommentLinksCodec {
  self: UserCodec ⇒

  implicit lazy val pullRequestReviewCommentsLinksDecoder = Decoder.instance { cursor ⇒
    for {
      self ← cursor.downField("self").downField("href").as[String]
      html ← cursor.downField("html").downField("href").as[String]
      pullRequest ← cursor.downField("pull_request").downField("href").as[String]
    } yield PullRequestReviewCommentLinks(self, html, pullRequest)
  }
}

case class PullRequestReviewCommentLinks(self: String, html: String, pullRequest: String)

trait LinksContentCodec {
  implicit lazy val linkContentDecoder = deriveDecoder[LinksContent]
}

case class LinksContent(self: String, html: String, git: String)

trait TreeCodec {
  implicit lazy val treeDecoder = deriveDecoder[Tree]
}

case class Tree(sha: String, url: String)
