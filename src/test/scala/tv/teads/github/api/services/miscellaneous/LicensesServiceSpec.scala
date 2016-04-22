package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.services.AbstractServiceSpec

class LicensesServiceSpec extends AbstractServiceSpec {

  "listAll" should {
    "be able to get the list of licenses" in {
      whenReady(teadsClient.miscellaneous.licenses.listAll) { licenses ⇒
        licenses should not be empty
        licenses.find(_.key == "mit") should not be empty // check that a well know license is in that list
      }
    }
  }

  "get" should {
    "return None when no matching license is found" in {
      whenReady(teadsClient.miscellaneous.licenses.get("foobarquz")) { license ⇒
        license shouldBe empty
      }
    }
    "be able to get MIT license details" in {
      whenReady(teadsClient.miscellaneous.licenses.get("mit")) { mit ⇒
        mit.value.key shouldBe "mit"
      }
    }
  }
}
