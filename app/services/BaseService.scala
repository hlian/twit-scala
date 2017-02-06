package com.originate.services

import com.originate.util.Logging

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * BaseService is used to share functions across all services.
 */
@SuppressWarnings(Array("org.wartremover.warts.Any"))
trait BaseService extends Logging {

  val toUnit: Any => Unit = (_ => ())

  def fs[T](t: T): Future[T] = Future.successful(t)

  def logResult[T](message: String): PartialFunction[Try[T], Unit] = {
    case Success(_) =>
      logger.info(message)
    case Failure(e) =>
      logger.error(e.getMessage)
  }

}
