package tv.teads.github.api.services

import tv.teads.github.api.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class TeamServiceSpec extends BaseSpec {

  override implicit val patienceConfig = PatienceConfig(30 seconds, 1 second)

  "Team Service" should "be able to fetch an organization teams" in {

    whenReady(TeamService.fetchOrgTeams("ebuzzing")) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch default organization teams" in {

    whenReady(TeamService.fetchDefaultOrgTeams) { list ⇒
      list should not be empty
    }
  }
  it should "be able to fetch teams member" in {

    whenReady(TeamService.fetchTeamMembers(1276819)) { list ⇒
      list should not be empty
    }
  }

}
