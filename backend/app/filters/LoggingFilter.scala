package com.originate.filters

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  val logger: Logger = Logger("http")

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader) map { result =>
      val duration = System.currentTimeMillis - startTime
      val method = requestHeader.method
      val ip = requestHeader.remoteAddress
      val version = requestHeader.version
      val status = result.header.status
      val size = result.body.contentLength getOrElse 0L

      logger.info(s"""$ip - "$method ${requestHeader.uri} $version" $status $size ${duration}ms""")

      result.withHeaders("Request-Time" -> duration.toString)
    }
  }

}
