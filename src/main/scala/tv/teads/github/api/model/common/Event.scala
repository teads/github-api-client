package tv.teads.github.api.model.common

import enumeratum.EnumEntry.Snakecase
import enumeratum._
import tv.teads.github.api.model.JsonEnum

sealed abstract class Event extends EnumEntry with Snakecase

object Event extends JsonEnum[Event] {
  override val values: Seq[Event] = findValues

  case object CommitComment extends Event
  case object Create extends Event
  case object Delete extends Event
  case object Deployment extends Event
  case object DeploymentStatus extends Event
  case object Download extends Event
  case object Follow extends Event
  case object Fork extends Event
  case object ForkApply extends Event
  case object Gist extends Event
  case object Gollum extends Event
  case object IssueComment extends Event
  case object Issues extends Event
  case object Member extends Event
  case object Membership extends Event
  case object PageBuild extends Event
  case object Public extends Event
  case object PullRequest extends Event
  case object PullRequestReviewComment extends Event
  case object Push extends Event
  case object Release extends Event
  case object Repository extends Event
  case object Status extends Event
  case object TeamAdd extends Event
  case object Watch extends Event

}