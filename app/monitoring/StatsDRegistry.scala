package com.originate.monitoring

import com.originate.global.Environment

/**
 * StatsDRegistry is used to create an instance of the StatsDService by weaving in several
 * untyped value members that Macwire would otherwise be unable to discern between. It
 * automatically prepends all keys with the application name, adds the environment tag to
 * all stats, and allows for an additonal arbitrary tag
 */
object StatsDRegistry {

  def createStatsDService(
      host: String,
      port: Int,
      applicationName: String,
      environment: Environment,
      optionalTag: Option[String] = None): StatsDService = {
    val statsDKeyPrefix = StatsKey(applicationName)
    val statsDDefaultTag = StatsTag("env", environment.toString)
    val statsDOptionalTag = optionalTag map (StatsTag("optional_tag", _))
    val statsDTags = Seq(Some(statsDDefaultTag), statsDOptionalTag) flatMap (_.toList)

    new StatsDService(
      StatsKey.normalize(statsDKeyPrefix),
      host,
      port,
      statsDTags: _*
    )
  }

}
