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
    val Array(dbUsername, dbPassword) = resolvedAuth
    val port = url.getPort
    val host = url.getHost
    val database = url.getPath

    private def resolvedAuth: Array[String] = {
      val explicitlyDeclared = for {
        u <- username
        p <- password
      } yield Array(u, p)
      val safeSplitUserInfo = url.getUserInfo.split(":").:+("").take(2)

      explicitlyDeclared getOrElse safeSplitUserInfo
    }
  }
}
