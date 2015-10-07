package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object OrganizationService extends GithubService with PayloadFormats {

  def fetchOrg(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] = {
    val url = s"orgs/$org"
    val errorMsg = s"Fetching organization $org failed"
    fetchOptional[Organization](url, errorMsg)
  }

  def fetchDefaultOrg(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] = {
    fetchOrg(configuration.organization)
  }

  def fetchUserOrgs(user: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Org]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"users/$user/orgs"
    val errorMsg = s"Fetching organizations for user $user failed"
    fetchMultiple[Org](url, errorMsg)
  }

}
