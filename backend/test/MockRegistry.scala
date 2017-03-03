package com.originate

import com.originate.config.Config
import com.originate.support.{MockDependencies, MockHelpers}
import com.originate.global.exceptions.ConfigurationLoadFailed

import pureconfig._

import java.sql.Connection
import scala.util.{Failure, Success}

trait MockRegistry extends MockHelpers with MockDependencies {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  implicit val connection = smartMock[Connection]

  implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
  val config: Config = loadConfig[Config] match {
    case Left(f) => throw ConfigurationLoadFailed(f)
    case Right(conf) => conf
  }

}
