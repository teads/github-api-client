package tv.teads.github.api.services

import okhttp3.Request
import io.circe._, io.circe.syntax._
import tv.teads.github.api.GithubApiClientConfig
import tv.teads.github.api.http._
import tv.teads.github.api.model._

import scala.concurrent.{ExecutionContext, Future}

object ReleaseService {

  implicit lazy val releaseParamEncoder = Encoder.instance[ReleaseParam] { param ⇒
    import param._
    Json.obj(
      "tag_name" → tagName.asJson,
      "target_commitish" → targetComitish.asJson,
      "name" → name.asJson,
      "body" → body.asJson,
      "draft" → draft.asJson,
      "prerelease" → prerelease.asJson
    )
  }

  case class ReleaseParam(
    tagName:        String,
    targetComitish: String,
    name:           String,
    body:           Option[String] = None,
    draft:          Boolean        = false,
    prerelease:     Boolean        = false
  )

}

class ReleaseService(config: GithubApiClientConfig) extends GithubService(config) with GithubApiCodecs {

  import ReleaseService._

  /**
   * @see https://developer.github.com/v3/repos/releases/#list-releases-for-a-repository
   * @param repository
   * @param ec
   * @return
   */
  def list(repository: String)(implicit ec: ExecutionContext): Future[List[Release]] =
    fetchMultiple[Release](
      s"repos/${config.owner}/$repository/releases",
      s"Fetching releases for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#get-a-single-release
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def get(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Option[Release]] =
    fetchOptional[Release](
      s"repos/${config.owner}/$repository/releases/$id",
      s"Fetching release $id for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#get-the-latest-release
   * @param repository
   * @param ec
   * @return
   */
  def getLatest(repository: String)(implicit ec: ExecutionContext): Future[Option[Release]] =
    fetchOptional[Release](
      s"repos/${config.owner}/$repository/releases/latest",
      s"Fetching latest release for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#get-a-release-by-tag-name
   * @param repository
   * @param tag
   * @param ec
   * @return
   */
  def getByTag(repository: String, tag: String)(implicit ec: ExecutionContext): Future[Option[Release]] =
    fetchOptional[Release](
      s"repos/${config.owner}/$repository/releases/tags/$tag",
      s"Fetching release by tag $tag for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#create-a-release
   * @param repository
   * @param release
   * @param ec
   * @return
   */
  def create(repository: String, release: ReleaseParam)(implicit ec: ExecutionContext): Future[Option[Release]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/releases"
    val requestBuilder = new Request.Builder().url(url).post(release.toJson)
    baseRequest(requestBuilder).map {
      _.as[Release].fold(
        code ⇒ failedRequest(s"Creating release $release for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/releases/#edit-a-release
   * @param repository
   * @param id
   * @param release
   * @param ec
   * @return
   */
  def edit(repository: String, id: Long, release: ReleaseParam)(implicit ec: ExecutionContext): Future[Option[Release]] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/releases/$id"
    val requestBuilder = new Request.Builder().url(url).patch(release.toJson)
    baseRequest(requestBuilder).map {
      _.as[Release].fold(
        code ⇒ failedRequest(s"Editing release $release for repository $repository failed", code, None),
        decodedResponse ⇒ Some(decodedResponse.decoded)
      )
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/releases/#delete-a-release
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def delete(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/releases/$id"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting release $id for repository $repository failed", response.code(), false)
    }
  }

  /**
   * @see https://developer.github.com/v3/repos/releases/#list-assets-for-a-release
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def listAssets(repository: String, id: Long)(implicit ec: ExecutionContext): Future[List[Asset]] =
    fetchMultiple[Asset](
      s"repos/${config.owner}/$repository/releases/$id/assets",
      s"Fetching assets for release $id for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#get-a-single-release-asset
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def getAsset(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Option[Asset]] =
    fetchOptional[Asset](
      s"repos/${config.owner}/$repository/releases/assets/$id",
      s"Fetching release asset $id for repository $repository failed"
    )

  /**
   * @see https://developer.github.com/v3/repos/releases/#delete-a-release-asset
   * @param repository
   * @param id
   * @param ec
   * @return
   */
  def deleteAsset(repository: String, id: Long)(implicit ec: ExecutionContext): Future[Boolean] = {
    val url = s"${config.apiUrl}/repos/${config.owner}/$repository/releases/assets/$id"
    val requestBuilder = new Request.Builder().url(url).delete()
    baseRequest(requestBuilder).map {
      case response if response.code() == 204 ⇒ true
      case response ⇒
        failedRequest(s"Deleting release asset $id for repository $repository failed", response.code(), false)
    }
  }
}
