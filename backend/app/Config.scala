package com.originate.config

import com.originate.global.Environment

import java.net.URI

case class Config(
    applicationName: String,
    environment: Environment,
    datadog: Datadog,
    db: DB
)

case class Datadog(
    agentHost: String,
    agentPort: Int
)

case class DB(
    default: DB.Default
)

object DB {
  case class Default(username: Option[String], password: Option[String], url: URI) {
    val port = url.getPort
    val host = url.getHost
    val database = url.getPath
  }
}
