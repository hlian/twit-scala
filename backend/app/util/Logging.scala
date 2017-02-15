package com.originate.util

import play.api.Logger

/**
 * Simple mixin trait to provide a logger to any class
 */
trait Logging {
  protected val logger: Logger = Logger(this.getClass())

  def logException(e: => Throwable): Unit = logger.error("exception:", e)
}
