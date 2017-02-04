package com.originate

import com.originate.config.Config
import com.originate.controllers.PingController

import com.softwaremill.macwire._
import com.typesafe.scalalogging.StrictLogging
import play.api.db.{BoneCPComponents, DBComponents}
import pureconfig._

import java.sql.Connection
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait Registry
  extends DBComponents
  with BoneCPComponents
  with StrictLogging {

  lazy val database = dbApi.database("default")
  implicit lazy val connection: Connection = database.getConnection

  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  val config: Config = loadConfig[Config] match {
    case Failure(f) => {
      logger.error(s"Failed to load config from application.conf: $f")
      throw f
    }
    case Success(conf) => conf
  }

  lazy val pingController = wire[PingController]

}
