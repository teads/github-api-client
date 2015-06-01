package tv.teads.github.api.services

import play.api.data.mapping.Write
import play.api.libs.json.{JsObject, JsValue}
import spray.http._
import tv.teads.github.api.models._
import tv.teads.github.api.services.GithubConfiguration.configuration
import tv.teads.github.api.util._

import scala.concurrent.{ExecutionContext, Future}

object IssueService extends GithubService {

  implicit lazy val issueParamJsonWrite: Write[IssueParam, JsValue] = {
    import play.api.data.mapping.json.Writes._
    Write.gen[IssueParam, JsObject]
  }

  case class IssueParam(title: String,
                        body: Option[String] = None,
                        assignee: Option[String] = None,
                        milestone: Option[String] = None,
                        labels: Set[String] = Set.empty)

  def create(repository: String, issue: IssueParam)(implicit ec: ExecutionContext): Future[Option[Issue]] = {
    val url = s"${configuration.api.url}/repos/${configuration.organization}/$repository/issues"

    baseRequest(url, Map.empty)
      .withHeader(HttpHeaders.Accept.name, RawContentMediaType)
      .postJsonInto[IssueParam, Issue](issue)
      .map {
      case SuccessfulRequest(i, _) ⇒ Some(i)
      case FailedRequest(statusCode) ⇒
        logger.error(s"Could not create issue, failed with status code ${statusCode.intValue}")
        None
    }
  }
}
