package tv.teads.github.api.services

import java.util.Base64

import scala.concurrent.{ExecutionContext, Future}

import okhttp3.{HttpUrl, Request}
import io.circe.generic.semiauto._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._
import tv.teads.github.api.util.IO.withCloseable

object ContentService extends GithubApiCodecs {
  implicit lazy val fileEditParamEncoder = deriveEncoder[FileEditParam]
  implicit lazy val fileCreateParamEncoder = deriveEncoder[FileCreateParam]

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

  def fetchFile(repository: String, path: String, branch: String = "master")(implicit ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/contents/$path"
    val httpUrl = HttpUrl.parse(url).newBuilder().addEncodedQueryParameter("ref", branch).build()
    val requestBuilder = new Request.Builder().url(httpUrl).get()
    baseRequest(requestBuilder, GithubMediaTypes.RawContentMediaType).map {
      case response if response.code() == 200 ⇒ Some(withCloseable(response.body())(_.string()))
      case response ⇒
        failedRequest(s"Fetching file at path $path in $repository failed", response.code(), None)
    }
  }

  def fetchReadme(repository: String, branch: String = "master")(implicit ec: ExecutionContext): Future[Option[String]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/readme"
    val httpUrl = HttpUrl.parse(url).newBuilder().addEncodedQueryParameter("ref", branch).build()
    val requestBuilder = new Request.Builder().url(httpUrl).get()
    baseRequest(requestBuilder, GithubMediaTypes.RawContentMediaType).map {
      case response if response.code() == 200 ⇒ Some(withCloseable(response.body())(_.string()))
      case response ⇒
        failedRequest(s"Fetching readme on branch $branch in $repository failed", response.code(), None)
    }
  }

  def createFile(repository: String, file: FileCreateParam)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/contents/${file.path}"
    val encodedFile = file.copy(content = Base64.getEncoder.encodeToString(file.content.getBytes))
    val requestBuilder = new Request.Builder().url(url).put(encodedFile.toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 200 ⇒ true
      case response ⇒
        failedRequest(s"Creating file ${file.path} in repository $repository failed", response.code(), false)
    }
  }

  def editFile(repository: String, file: FileEditParam)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/contents/${file.path}"
    val encodedFile = file.copy(content = Base64.getEncoder.encodeToString(file.content.getBytes))
    val requestBuilder = new Request.Builder().url(url).put(encodedFile.toJson)
    baseRequest(requestBuilder).map {
      case response if response.code() == 200 ⇒ true
      case response ⇒
        failedRequest(s"Editing file ${file.path} in repository $repository failed", response.code(), false)
    }
  }

  def deleteFile(repository: String, file: FileEditParam)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/contents/${file.path}"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 200 ⇒ true
      case response ⇒
        failedRequest(s"Deleting file ${file.path} in $repository failed", response.code(), false)
    }
  }

}
