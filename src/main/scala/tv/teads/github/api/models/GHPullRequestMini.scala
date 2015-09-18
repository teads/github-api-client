package tv.teads.github.api.models

import org.joda.time.DateTime

case class GHPullRequestMini(
  project:   String,
  url:       String,
  title:     String,
  state:     String,
  user:      User,
  body:      String,
  createdAt: DateTime,
  updatedAt: DateTime,
  closedAt:  Option[DateTime],
  mergedAt:  Option[DateTime],
  assignee:  Option[User],
  branch:    String,
  tags:      Set[String]
)

