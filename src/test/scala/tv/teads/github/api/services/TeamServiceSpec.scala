package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class TeamServiceSpec extends BaseSpec {

  "Team Service" should "be able to fetch an organization teams" in {

    whenReady(ebuzzingClient.teams.fetchOrgTeams) { list ⇒
      list should not be empty
    }
  }

  it should "be able to fetch teams member" in {

    whenReady(ebuzzingClient.teams.fetchTeamMembers(1276819)) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch a team" in {

    whenReady(ebuzzingClient.teams.fetchTeam(1276819)) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch teams repositories" in {

    whenReady(ebuzzingClient.teams.fetchTeamRepos(1600886)) { list ⇒
      list should not be empty
    }
  }

}
