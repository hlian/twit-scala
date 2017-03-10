package com.originate.support

import org.scalatest.Matchers._
import play.api.libs.json.{JsDefined, JsUndefined, Json, Writes}
import play.api.libs.ws.WSResponse

/**
 * A utility object that holds all implicit conversion ops used for testing
 */
object Ops {

  implicit class JsonBodyMatcher(val left: WSResponse) extends AnyVal {
    def shouldHaveJsonBody[A : Writes](right: A): Unit = {
      val body = (Json.parse(left.body) \ "data")(0)
      val correctJson = Json.toJson(right)
      body match {
        case JsDefined(js) => js shouldBe correctJson
        case undefined: JsUndefined => fail(undefined.error)
      }
    }
  }

}
