package tv.teads.github.api.models

import org.joda.time.DateTime

case class GHPullRequestMini(
  project:    String,
  url:        String,
  title:      String,
  state:      String,
  user:       User,
  body:       String,
  created_at: DateTime,
  updated_at: DateTime,
  closed_at:  Option[DateTime],
  merged_at:  Option[DateTime],
  assignee:   Option[User],
  branch:     String,
  tags:       Set[String]
)

