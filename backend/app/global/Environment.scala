package com.originate.global

import pureconfig.ConfigConvert.viaString
import pureconfig.ConfigReader
import pureconfig.ConvertHelpers.catchReadError

case class Environment(value: String) extends AnyVal {
  override def toString: String = value

  def isLocal: Boolean = value match {
    case "local" | "test" => true
    case _ => false
  }
}

object Environment {

  implicit val readEnvironment: ConfigReader[Environment] =
    viaString(catchReadError(Environment.apply), (_.toString))

}
