package com.originate.global

import pureconfig.ConfigConvert
import pureconfig.ConfigConvert.{fromStringReader, catchReadError}

case class Environment(value: String) extends AnyVal {
  override def toString: String = value

  def isLocal: Boolean = value match {
    case "local" | "test" => true
    case _ => false
  }
}

object Environment {

  implicit val readEnvironment: ConfigConvert[Environment] =
    fromStringReader(catchReadError(x => Environment(x)))

}
