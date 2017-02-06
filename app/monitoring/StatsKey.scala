package com.originate.monitoring

import mojolly.inflector.InflectorImports._

/**
 * Datadog keys have some subtleties that are explained in their docs. If a metric isn't showing up,
 * but you're positive it's being sent and other metrics are being sent, doublecheck the formatting:
 * http://docs.datadoghq.com/faq/#api
 */
case class StatsKey(value: String) extends AnyVal

object StatsKey {
  private val ValidPattern = """[a-zA-Z0-9_.]+""".r

  def normalize(key: StatsKey): String = key.value.split("\\.") map (_.trim) map (_.underscore) mkString "."

  def isValid(key: StatsKey): Boolean = isValidString(key.value)

  def isValidString(key: String): Boolean = ValidPattern.pattern.matcher(key).matches
}

case class StatsTag(key: String, value: String) {
  override def toString: String = s"$key:$value"
}
