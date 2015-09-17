package tv.teads.github.api.services

import akka.actor.ActorRefFactory
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object OrganizationService extends GithubService {

  def fetchOrg(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] = {
    val url = s"${configuration.api.url}/orgs/$org"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty).executeRequestInto[Option[Organization]]().map {
      case SuccessfulRequest(o, _) ⇒ o
      case FailedRequest(statusCode) ⇒
        logger.error(s"Fetching organization $org failed, status code: ${statusCode.intValue}")
        None
    }
  }

  def fetchDefaultOrg(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] = {
    fetchOrg(configuration.organization)
  }

  def fetchUserOrgs(user: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Org]] = {
    import play.api.data.mapping.json.Rules._
    val url = s"${configuration.api.url}/users/$user/orgs"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty).executeRequestInto[List[Org]]().map {
      case SuccessfulRequest(orgs, _) ⇒ orgs
      case FailedRequest(statusCode) ⇒
        logger.error(s"Fetching orgs for user $user failed, status code: ${statusCode.intValue}")
        Nil
    }
  }

}
