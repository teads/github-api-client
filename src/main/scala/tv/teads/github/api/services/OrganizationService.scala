package tv.teads.github.api.services

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorRefFactory
import tv.teads.github.api.Configuration.configuration
import tv.teads.github.api.model._

object OrganizationService extends GithubService with GithubApiCodecs {

  def fetchOrg(org: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] =
    fetchOptional[Organization](s"orgs/$org", s"Fetching organization $org failed")

  def fetchDefaultOrg(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[Organization]] =
    fetchOrg(configuration.organization)

  def fetchUserOrgs(user: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[List[Org]] =
    fetchMultiple[Org](s"users/$user/orgs", s"Fetching organizations for user $user failed")

}
