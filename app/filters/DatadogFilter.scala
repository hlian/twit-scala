package com.originate.filters

import com.originate.monitoring.{StatsDService, StatsKey, StatsTag}

import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

class DatadogFilter(statsd: StatsDService)(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader) andThen { case tryResult =>
      val duration = System.currentTimeMillis - startTime
      val pathPartTags = requestHeader.path.split("/").zipWithIndex collect {
        case (part, i) if StatsKey.isValidString(part) => StatsTag(s"path_part_$i", part)
      }
      val baseTags = Seq(
        StatsTag("path", "root" + requestHeader.path),
        StatsTag("method", requestHeader.method)
      ) ++ pathPartTags

      tryResult map { result =>
        val key = StatsKey("http.request")
        val tags = baseTags ++ Seq(
          StatsTag("status", result.header.status.toString),
          StatsTag("status_group", (result.header.status/100).toString)
        )

        statsd.time(key, duration, tags: _*)
        statsd.increment(key, tags: _*)
      } recover { case exception =>
        statsd.increment(
          StatsKey("http.request.failure"),
          (baseTags :+ StatsTag("exception", exception.getMessage)): _*
        )
      }
    }
  }

}

