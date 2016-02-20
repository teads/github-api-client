package tv.teads.github.api.services

import cats.syntax.option._
import io.circe.generic.semiauto._
import okhttp3.Request

import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._
import tv.teads.github.api.http._

import scala.concurrent.{Future, ExecutionContext}

object ReferenceService {
  implicit lazy val createReferenceParamEncoder = deriveEncoder[CreateReferenceParam]
  case class CreateReferenceParam(ref: String, sha: String)

  implicit lazy val updateReferenceParamEncoder = deriveEncoder[UpdateReferenceParam]
  case class UpdateReferenceParam(sha: String, force: Boolean)
}
class ReferenceService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import ReferenceService._

  def fetch(repository: String, ref: String)(implicit ec: ExecutionContext): Future[Option[Reference]] =
    fetchOptional[Reference](
      s"repos/${config.owner}/$repository/git/refs/$ref",
      s"Fetching reference $ref in repository $repository failed"
    )

  def fetchAll(repository: String, typeFilter: Option[String])(implicit ec: ExecutionContext): Future[List[Reference]] =
    fetchMultiple[Reference](
      s"repos/${config.owner}/$repository/git/refs${typeFilter.map("/" + _).getOrElse("")}",
      s"Fetching references in repository $repository failed"
    )

  def create(repository: String, param: CreateReferenceParam)(implicit ec: ExecutionContext): Future[Option[Reference]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/git/refs"
    val requestBuilder = new Request.Builder().url(url).post(param.toJson)
    baseRequest(requestBuilder).map {
      _.as[Reference].fold(
        code ⇒ failedRequest(s"Creating reference $param on $repository failed", code, None),
        _.decoded.some
      )
    }
  }

  def update(repository: String, ref: String, param: UpdateReferenceParam)(implicit ec: ExecutionContext): Future[Option[Reference]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/git/refs/$ref"
    val requestBuilder = new Request.Builder().url(url).patch(param.toJson)
    baseRequest(requestBuilder).map {
      _.as[Reference].fold(
        code ⇒ failedRequest(s"Updating reference with $param on $repository failed", code, None),
        _.decoded.some
      )
    }
  }

  def delete(repository: String, ref: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/git/refs/$ref"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting reference $ref in repository $repository failed", response.code(), false)
    }
  }
}
