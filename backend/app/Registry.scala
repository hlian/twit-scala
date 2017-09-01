package com.originate

import com.originate.config.Config
import com.originate.controllers._
import com.originate.global.exceptions.ConfigurationLoadFailed
import com.originate.monitoring.StatsDRegistry

import _root_.controllers.Assets
import com.softwaremill.macwire._
import play.api.BuiltInComponents
import play.api.db.{BoneCPComponents, DBComponents}
import play.api.mvc.ControllerComponents
import pureconfig._

trait Registry
  extends DBComponents
  with BuiltInComponents
  with BoneCPComponents {

  def assets: Assets
  def controllerComponents: ControllerComponents

  implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
  val config: Config = loadConfig[Config] match {
    case Left(f) => throw ConfigurationLoadFailed(f)
    case Right(conf) => conf
  }

  lazy val assetsController = wire[AssetsController]
  lazy val pingController = wire[PingController]
  lazy val swaggerController = wire[SwaggerController]

  lazy val statsd = StatsDRegistry.createStatsDService(
    config.datadog.agentHost,
    config.datadog.agentPort,
    config.applicationName,
    config.environment
  )

}
