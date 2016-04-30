package tv.teads.github.api.services.repositories

import cats.syntax.option._
import tv.teads.github.api.model.common.Event
import tv.teads.github.api.model.repositories.EditWebhookRequest
import tv.teads.github.api.services.AbstractServiceSpec

class RepositoriesWebhooksServiceSpec extends AbstractServiceSpec {

  "listAll" should {
    "be able to fetch repository webhooks for github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.listAll("github-api-client_test")) { webhooks ⇒
        webhooks.value.find(_.name == "travis") should not be empty
      }
    }

    "not be able to fetch repository webhooks for a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.listAll("foobarquz")) { webhooks ⇒
        webhooks shouldBe empty
      }
    }
  }

  "listSinglePage" should {
    "be able to fetch the first page of repository webhooks for github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.listSinglePage("github-api-client_test", 1)) { webhooks ⇒
        webhooks.value.find(_.name == "travis") should not be empty
      }
    }

    "not be able to fetch the first page of repository webhooks for a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.listSinglePage("foobarquz", 1)) { webhooks ⇒
        webhooks shouldBe empty
      }
    }
  }

  "get" should {
    "be able to an existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.listAll("github-api-client_test")) { webhooks ⇒
        val id = webhooks.value.find(_.name == "travis").value.id
        whenReady(teadsClient.repositories.webhooks.get("github-api-client_test", id)) { travis ⇒
          travis.value.name shouldBe "travis"
          travis.value.id shouldBe id
        }
      }
    }

    "not be able to send a test push to a non existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.get("github-api-client_test", -1)) { webhook ⇒
        webhook shouldBe empty
      }
    }

    "not be able to send a test push to a non existing webhook on a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.get("foobarquz", -1)) { webhook ⇒
        webhook shouldBe empty
      }
    }
  }

  // TODO
  /*"create" should {
    "" in {

    }

    "" in {

    }
  }*/

  "edit" should {
    // TODO: good case

    "not be able to edit a non existing webhook on github-api-client_test" in {
      val request = EditWebhookRequest(active = false.some, addEvents = List(Event.Gollum).some)
      whenReady(teadsClient.repositories.webhooks.edit("github-api-client_test", -1, request)) { response ⇒
        response shouldBe empty
      }
    }

    "not be able to edit a non existing webhook on a non existing repository" in {
      val request = EditWebhookRequest(active = false.some, addEvents = List(Event.Gollum).some)
      whenReady(teadsClient.repositories.webhooks.edit("foobarquz", -1, request)) { response ⇒
        response shouldBe empty
      }
    }
  }

  "delete" should {
    // TODO: good case

    "not be able to delete a non existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.delete("github-api-client_test", -1)) { response ⇒
        response shouldBe false
      }
    }

    "not be able to delete a non existing webhook on a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.delete("foobarquz", -1)) { response ⇒
        response shouldBe false
      }
    }
  }

  "testPush" should {
    "be able to send a test push to existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.listAll("github-api-client_test")) { webhooks ⇒
        val id = webhooks.value.find(_.name == "travis").value.id
        whenReady(teadsClient.repositories.webhooks.testPush("github-api-client_test", id)) { response ⇒
          response shouldBe true
        }
      }
    }

    "not be able to send a test push to a non existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.testPush("github-api-client_test", -1)) { response ⇒
        response shouldBe false
      }
    }

    "not be able to send a test push to a non existing webhook on a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.testPush("foobarquz", -1)) { response ⇒
        response shouldBe false
      }
    }
  }

  "ping" should {
    "be able an ping existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.listAll("github-api-client_test")) { webhooks ⇒
        val id = webhooks.value.find(_.name == "travis").value.id
        whenReady(teadsClient.repositories.webhooks.ping("github-api-client_test", id)) { response ⇒
          response shouldBe true
        }
      }
    }

    "not be able to ping a non existing webhook on github-api-client_test" in {
      whenReady(teadsClient.repositories.webhooks.ping("github-api-client_test", -1)) { response ⇒
        response shouldBe false
      }
    }

    "not be able to ping a non existing webhook on a non existing repository" in {
      whenReady(teadsClient.repositories.webhooks.ping("foobarquz", -1)) { response ⇒
        response shouldBe false
      }
    }
  }

}
