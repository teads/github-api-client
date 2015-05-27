package tv.teads.github.api.util

import org.slf4j.LoggerFactory
import org.slf4j.Logger

trait LazyLogging {

  protected lazy val logger: Logger = LoggerFactory.getLogger(getClass.getName)
}

