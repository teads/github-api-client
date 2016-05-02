package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.services.AbstractServiceSpec

class EmojisServiceSpec extends AbstractServiceSpec {

  "listAll" should {
    "be able to get the list of emojis" in {
      whenReady(teadsClient.miscellaneous.emojis.listAll) { emojis ⇒
        emojis should not be empty
        emojis should contain key "+1" // Check that a well known emoji is in that map
      }
    }
  }

}
