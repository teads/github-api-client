package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.services.AbstractServiceSpec

class GitignoreServiceSpec extends AbstractServiceSpec {

  "listAll" should {
    "be able to get the list of all gitignore templates" in {
      whenReady(teadsClient.miscellaneous.gitignore.listAll) { tpl ⇒
        tpl should not be empty
        tpl should contain("Scala")
      }
    }
  }

  "get" should {
    "return None when no matching gitignore template is found" in {
      whenReady(teadsClient.miscellaneous.gitignore.get("foobarquz")) { tpl ⇒
        tpl shouldBe empty
      }
    }

    "return the gitignore template description when the template is found" in {
      whenReady(teadsClient.miscellaneous.gitignore.get("Scala")) { tpl ⇒
        tpl.value.name shouldBe "Scala"
      }
    }
  }

  "getRaw" should {
    "return None when no matching gitignore template is found" in {
      whenReady(teadsClient.miscellaneous.gitignore.getRaw("foobarquz")) { tpl ⇒
        tpl shouldBe empty
      }
    }

    "return the gitignore template description when the template is found" in {
      whenReady(teadsClient.miscellaneous.gitignore.getRaw("Scala")) { tpl ⇒
        tpl.value should not be empty
      }
    }
  }

}
