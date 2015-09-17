package tv.teads.github.api.models

import tv.teads.github.api.models.common.ADTEnum

object Privacies {

  sealed trait Privacy

  object Privacy extends ADTEnum[Privacy] {

    case object secret extends Privacy
    case object closed extends Privacy

    val list = Seq(
      secret,
      closed
    )
  }

}
