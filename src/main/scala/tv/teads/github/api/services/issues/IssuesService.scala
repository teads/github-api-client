package tv.teads.github.api.services.issues

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.services.AbstractGithubService

class IssuesService(config: GithubApiClientConfig) extends AbstractGithubService(config) {

  val assignees = new IssuesAssigneesService(config)
  val comments = new IssuesCommentsService(config)
  val events = new IssuesEventsService(config)
  val labels = new IssuesLabelsService(config)
  val milestones = new IssuesMilestonesService(config)

}
