package tv.teads.github.api.services

import java.util.Base64

import akka.actor.ActorRefFactory
import play.api.data.mapping.Write
import play.api.libs.json.{ JsObject, JsValue }
import spray.http._
import spray.httpx.RequestBuilding._
import tv.teads.github.api.models._
import tv.teads.github.api.models.payloads.PayloadFormats
import tv.teads.github.api.services.Configuration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ ExecutionContext, Future }

object ContentService extends GithubService with PayloadFormats {

  def fetchFile(org: String, repository: String, path: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${configuration.url}/repos/$org/$repository/contents/$path"
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

  def fetchFileDefaultOrg(repository: String, path: String)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    fetchFile(configuration.organization, repository, path)
  }

  def fetchReadme(org: String, repository: String, branch: String = "master")(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${configuration.url}/repos/$org/$repository/readme"
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

  def fetchReadmeDefaultOrg(repository: String, branch: String = "master")(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Option[String]] = {
    fetchReadme(configuration.organization, repository, branch)
  }

  case class FileCreateParam(
    path:      String,
    message:   String,
    content:   String,
    branch:    Option[String] = None,
    committer: Option[Author],
    author:    Option[Author]
  )

  implicit lazy val fileCreateParamJsonWrite: Write[FileCreateParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[FileCreateParam, JsObject]
  }

  def createFile(org: String, repository: String, file: FileCreateParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/content/${file.path}"
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

  case class FileEditParam(
    path:      String,
    message:   String,
    content:   String,
    sha:       String,
    branch:    Option[String] = None,
    committer: Option[Author],
    author:    Option[Author]
  )

  implicit lazy val fileEditParamJsonWrite: Write[FileEditParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[FileEditParam, JsObject]
  }

  def editFile(org: String, repository: String, file: FileEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/content/${file.path}"
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

  def deleteFile(org: String, repository: String, file: FileEditParam)(implicit refFactory: ActorRefFactory, ec: ExecutionContext): Future[Boolean] = {
    val url = s"${configuration.url}/repos/$org/$repository/content/${file.path}"
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
