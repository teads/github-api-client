package tv.teads.github.api.services.miscellaneous

import tv.teads.github.api.model.miscellaneous.{MarkdownRenderMode, MarkdownRenderingRequest}
import tv.teads.github.api.services.AbstractServiceSpec

class MarkdownServiceSpec extends AbstractServiceSpec {

  "render" should {
    "be able to render plain Markdown" in {
      val text = "*italic* **strong** Text"
      val request = MarkdownRenderingRequest(text, "teads/github-api-client_test")

      whenReady(teadsClient.miscellaneous.markdown.render(request)) { md ⇒
        md shouldBe "<p><em>italic</em> <strong>strong</strong> Text</p>\n"
      }
    }

    "be able to render Github Flavored Markdown" in {
      val text = "Commit 6f9082b7d99ba5af109d55ce8972afd4a50b8442"
      val gfm = MarkdownRenderMode.GithubFlavoredMarkdown
      val request = MarkdownRenderingRequest(text, "teads/github-api-client_test", gfm)

      whenReady(teadsClient.miscellaneous.markdown.render(request)) { md ⇒
        md shouldBe
          "<p>Commit <a href=\"https://github.com/teads/github-api-client_test/commit/6f9082b7d99ba5af109d55ce8972afd4a50b8442\" class=\"commit-link\"><tt>6f9082b</tt></a></p>"
      }
    }
  }

  "renderRaw" should {
    "be able to render plain Markdown" in {
      val text = "*italic* **strong** Text"
      whenReady(teadsClient.miscellaneous.markdown.renderRaw(text)) { md ⇒
        md shouldBe "<p><em>italic</em> <strong>strong</strong> Text</p>\n"
      }
    }
  }
}
