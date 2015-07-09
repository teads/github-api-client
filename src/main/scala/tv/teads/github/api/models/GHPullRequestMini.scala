package tv.teads.github.api.models

case class GHPullRequestMini(
  project:    String,
  url:        String,
  title:      String,
  state:      String,
  user:       User,
  body:       String,
  created_at: String,
  updated_at: String,
  closed_at:  Option[String],
  merged_at:  Option[String],
  assignee:   Option[User],
  branch:     String,
  tags:       Set[String]
)

