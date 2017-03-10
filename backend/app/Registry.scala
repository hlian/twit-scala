package com.originate

import com.originate.config.Config
import com.originate.controllers._
import com.originate.global.exceptions.ConfigurationLoadFailed
import com.originate.monitoring.StatsDRegistry

import com.softwaremill.macwire._
import play.api.BuiltInComponents
import play.api.db.{BoneCPComponents, DBComponents}
import pureconfig._

import java.sql.Connection
import scala.concurrent.ExecutionContext

trait Registry
  extends DBComponents
  with BuiltInComponents
  with BoneCPComponents {

  lazy val database = dbApi.database("default")
  implicit lazy val connection: Connection = database.getConnection

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
  val config: Config = loadConfig[Config] match {
    case Left(f) => throw ConfigurationLoadFailed(f)
    case Right(conf) => conf
  }

  lazy val pingController = wire[PingController]
  lazy val swaggerController = wire[SwaggerController]

  lazy val statsd = StatsDRegistry.createStatsDService(
    config.datadog.agentHost,
    config.datadog.agentPort,
    config.applicationName,
    config.environment
  )

}
