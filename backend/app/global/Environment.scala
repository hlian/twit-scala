package com.originate.global

case class Environment(value: String) extends AnyVal {
  override def toString: String = value

  def isLocal: Boolean = value match {
    case "local" | "test" => true
    case _ => false
  }
}
