package com.originate.util

import play.api.Logger

trait Logging {
  protected val logger: Logger = Logger(this.getClass())

  def logException(e: => Throwable): Unit = logger.error("exception:", e)
}
