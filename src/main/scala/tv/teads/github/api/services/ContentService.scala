package tv.teads.github.api.services

import java.util.Base64

import akka.actor.ActorRefFactory
import io.circe.generic.semiauto._
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

object ContentService extends GithubApiCodecs {
  implicit lazy val fileEditParamEncoder = deriveFor[FileEditParam].encoder
  implicit lazy val fileCreateParamEncoder = deriveFor[FileCreateParam].encoder

  case class FileCreateParam(
    path:      String,
    message:   String,
    content:   String,
    branch:    Option[String] = None,
    committer: Option[Author],
    author:    Option[Author]
  )

  case class FileEditParam(
    path:      String,
    message:   String,
    content:   String,
    sha:       String,
    branch:    Option[String] = None,
    committer: Option[Author],
    author:    Option[Author]
  )
}
class ContentService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {
  import ContentService._

  def fetchFile(repository: String, path: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/contents/$path"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          Some(response.entity.asString)

        case response ⇒
          logger.error(s"fetchFile $path with url $url failed with status code ${response.status.intValue}")
          None
      }
  }

  def fetchReadme(repository: String, branch: String = "master")(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/readme"
    val req: HttpRequest = Get(url)
    baseRequest(req, Map("ref" → branch))
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          Some(response.entity.asString)

        case response ⇒
          logger.error(s"fetchReadme with url $url failed with status code ${response.status.intValue}")
          None
      }
  }

  def createFile(repository: String, file: FileCreateParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/content/${file.path}"
    val req: HttpRequest = Put(url, file.copy(content = Base64.getEncoder.encodeToString(file.content.getBytes)))
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          true

        case response ⇒
          logger.error(s"cannot create file  with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def editFile(repository: String, file: FileEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/content/${file.path}"
    val req: HttpRequest = Put(url, file.copy(content = Base64.getEncoder.encodeToString(file.content.getBytes)))
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          true

        case response ⇒
          logger.error(s"Cannot edit file ${file.path} with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

  def deleteFile(repository: String, file: FileEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/content/${file.path}"
    val req: HttpRequest = Delete(url)
    baseRequest(req, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .executeRequest()
      .map {
        case response if response.status == StatusCodes.OK ⇒
          true

        case response ⇒
          logger.error(s"Cannot delete file ${file.path} with url $url failed with status code ${response.status.intValue}")
          false
      }
  }

}
