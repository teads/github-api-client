package tv.teads.github.api.services

import okhttp3.Request
import io.circe.Json
import io.circe.generic.semiauto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.filters._
import tv.teads.github.api.http._
import tv.teads.github.api.model._
import tv.teads.github.api.util._
import tv.teads.github.api.util.CaseClassToMap._

import scala.concurrent.{ExecutionContext, Future}

object DeploymentService {

  implicit lazy val deploymentParamEncoder = deriveEncoder[DeploymentParam]
  implicit lazy val deploymentStatusParamEncoder = deriveEncoder[DeploymentStatusParam]

  case class DeploymentFilter(
    sha:         Option[String] = None,
    ref:         Option[String] = None,
    task:        Option[String] = None,
    environment: Option[String] = None
  )

  case class DeploymentParam(
    ref:               String,
    task:              Option[String]       = Some("deploy"),
    auto_merge:        Option[Boolean]      = Some(true),
    required_contexts: Option[List[String]] = None,
    payload:           Option[Json]         = None,
    environment:       Option[String]       = Some("production"),
    description:       Option[String]       = None
  )

  case class DeploymentStatusParam(
    state:       DeploymentStatusState,
    target_url:  Option[String]        = None,
    description: Option[String]        = None
  )

}

class DeploymentService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  import DeploymentService._

  def list(repository: String, filter: DeploymentFilter)(implicit ec: ExecutionContext): Future[List[Deployment]] =
    fetchMultiple[Deployment](
      s"repos/${config.owner}/$repository/deployments",
      s"Fetching repository $repository deployments failed",
      filter.toMapStringified
    )

  def create(repository: String, deployment: DeploymentParam)(implicit ec: ExecutionContext): Future[Option[Deployment]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/deployments"
    val requestBuilder = new Request.Builder().url(url).post(deployment.toJson)
    baseRequest(requestBuilder).map {
      _.as[Deployment].fold(
        code ⇒ failedRequest(s"Creating deployment $deployment for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  def listStatuses(repository: String, deploymentId: Long)(implicit ec: ExecutionContext): Future[List[DeploymentStatus]] =
    fetchMultiple[DeploymentStatus](
      s"repos/${config.owner}/$repository/deployments/$deploymentId/statuses",
      s"Fetching repository $repository deployment $deploymentId statuses failed"
    )

  def createStatus(repository: String, deploymentId: Long, deploymentStatus: DeploymentStatusParam)(implicit ec: ExecutionContext): Future[Option[DeploymentStatus]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/deployments/$deploymentId/statuses"
    val requestBuilder = new Request.Builder().url(url).post(deploymentStatus.toJson)
    baseRequest(requestBuilder).map {
      _.as[DeploymentStatus].fold(
        code ⇒ failedRequest(s"Creating deployment status $deploymentStatus for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

}
