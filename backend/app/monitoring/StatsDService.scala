package com.originate.monitoring

import com.originate.util.Tap._

import com.timgroup.statsd.{Event, NonBlockingStatsDClient, StatsDClient}

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * This service is a wrapper around the StatsDClient that we use. Its purpose is to allow us to add
 *   extra usage to the client, as well as to enforce restrictions on the way that we name our keys.
 */
class StatsDService(prefix: String, host: String, port: Int, tags: StatsTag*) {

  val client: StatsDClient = new NonBlockingStatsDClient(
    prefix,
    host,
    port,
    tags map (_.toString): _*
  )

  private val keyMap: TrieMap[StatsKey, String] = TrieMap.empty

  private val tagMap: TrieMap[StatsTag, String] = TrieMap.empty

  def count(key: StatsKey, delta: Long, tags: StatsTag*): Unit =
    client.count(getString(key), delta, toTagStrings(tags): _*)

  def increment(key: StatsKey, tags: StatsTag*): Unit =
    client.increment(getString(key), toTagStrings(tags): _*)

  def decrement(key: StatsKey, tags: StatsTag*): Unit =
    client.decrement(getString(key), toTagStrings(tags): _*)

  def gauge(key: StatsKey, value: Double, tags: StatsTag*): Unit =
    client.gauge(getString(key), value, toTagStrings(tags): _*)

  def gauge(key: StatsKey, value: Long, tags: StatsTag*): Unit =
    client.gauge(getString(key), value, toTagStrings(tags): _*)

  def time(key: StatsKey, value: Long, tags: StatsTag*): Unit =
    client.time(getString(key), value, toTagStrings(tags): _*)

  def timeBlock[T](key: StatsKey, tags: StatsTag*)(body: => T): T = {
    val start = System.currentTimeMillis
    val res = Try(body)
    recordTime(key, System.currentTimeMillis - start, tags)(res)
    res match {
      case Success(t) => t
      case Failure(e) => throw e
    }
  }

  def timeFuture[T](
      key: StatsKey,
      tags: StatsTag*)
      (f: => Future[T])
      (implicit ec: ExecutionContext): Future[T] = {
    val start = System.currentTimeMillis
    f andThen recordTime(key, System.currentTimeMillis - start, tags)
  }

  private def recordTime[T](key: StatsKey, timeMs: Long, tags: Seq[StatsTag]): PartialFunction[Try[T], Unit] = {
    case Success(_) => client.time(
      getString(key) + "_success",
      timeMs,
      toTagStrings(tags): _*
    )
    case Failure(_) => client.time(
      getString(key) + "_failure",
      timeMs,
      toTagStrings(tags): _*
    )
  }

  def histogram(key: StatsKey, value: Double, tags: StatsTag*): Unit =
    client.histogram(getString(key), value, toTagStrings(tags): _*)

  def histogram(key: StatsKey, value: Long, tags: StatsTag*): Unit =
    client.histogram(getString(key), value, toTagStrings(tags): _*)

  def event(title: String, text: String, tags: StatsTag*): Unit = {
    val event = Event.builder.withTitle(title).withText(text).build
    client.recordEvent(event, toTagStrings(tags): _*)
  }

  private def toTagStrings(tags: Seq[StatsTag]): Seq[String] =
    tags map getTagString

  private def getTagString(tag: StatsTag): String =
    tagMap get tag getOrElse {
      tag.toString tap (tagMap.put(tag, _))
    }

  private def getString(statsKey: StatsKey): String =
    keyMap get statsKey getOrElse {
      StatsKey.normalize(statsKey) tap { normalized =>
        keyMap.put(statsKey, normalized)
      }
    }

}
