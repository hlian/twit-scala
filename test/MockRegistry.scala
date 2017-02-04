package com.originate

import com.originate.config.Config
import com.originate.support.{MockDependencies, MockHelpers}

import pureconfig._

import java.sql.Connection
import scala.util.{Failure, Success}

trait MockRegistry extends MockHelpers with MockDependencies {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  implicit val connection = smartMock[Connection]

  val config: Config = loadConfig[Config] match {
    case Failure(f) => throw f
    case Success(conf) => conf
  }

}
