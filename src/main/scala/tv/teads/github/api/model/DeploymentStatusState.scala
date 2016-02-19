package tv.teads.github.api.model

import tv.teads.github.api.util.Enumerated

sealed trait DeploymentStatusState
object DeploymentStatusState extends Enumerated[DeploymentStatusState] {
  val values = List(pending, success, error, failure)

  case object pending extends DeploymentStatusState
  case object success extends DeploymentStatusState
  case object error extends DeploymentStatusState
  case object failure extends DeploymentStatusState
}