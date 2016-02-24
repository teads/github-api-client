package tv.teads.github.api.services

import io.circe._, io.circe.generic.auto._, io.circe.jawn._, io.circe.syntax._
import tv.teads.github.api.BaseSpec
import tv.teads.github.api.services.DeploymentService.{DeploymentStatusParam, DeploymentParam, DeploymentFilter}
import tv.teads.github.api.model.DeploymentStatusState

import scala.concurrent.ExecutionContext.Implicits.global

class DeploymentServiceSpec extends BaseSpec {

  "Deployment Service" should "create a deployment with payload for github-api-client repository branch test-pr" in {
    val json =
      """
        |{
        |"key1":"value1",
        |"key2":\"value2"
        |}
      """.stripMargin

    val deployment = DeploymentParam(
      ref = "test-pr",
      task = Some("github-api-client/DeploymentServiceSpec"),
      description = Some("github-api-client DeploymentServiceSpec Unit test - create"),
      environment = Some("test"),
      auto_merge = Some(false),
      payload = Some(parse(json).getOrElse(Json.empty))

    )

    whenReady(teadsClient.deployments.create("github-api-client", deployment)) { opt ⇒
      opt should not be empty
    }
  }

  it should "create a deployment with empty payload for github-api-client repository branch test-pr" in {

    val deployment = DeploymentParam(
      ref = "test-pr",
      task = Some("github-api-client/DeploymentServiceSpec"),
      description = Some("github-api-client DeploymentServiceSpec Unit test - create with empty payload"),
      environment = Some("test"),
      auto_merge = Some(false)
    )

    whenReady(teadsClient.deployments.create("github-api-client", deployment)) { opt ⇒
      opt should not be empty
    }
  }

  it should "be able to fetch github-api-client deployments" in {

    whenReady(teadsClient.deployments.list("github-api-client", DeploymentFilter())) { list ⇒
      list should not be empty
    }
  }

  it should "create a deployment status" in {
    val status = DeploymentStatusParam(
      state = DeploymentStatusState.error,
      description = Some("github-api-client/DeploymentServiceSpec Unit test - create Deployment Status"),
      target_url = Some("https://www.google.com")
    )

    whenReady(teadsClient.deployments.createStatus("github-api-client", 3775791, status)) { opt ⇒
      opt should not be empty
    }
  }

  it should "be able to fetch github-api-client deployments statuses" in {

    whenReady(teadsClient.deployments.listStatuses("github-api-client", 3775791)) { list ⇒
      list should not be empty
    }
  }

}
