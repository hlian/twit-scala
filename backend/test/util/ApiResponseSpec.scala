package com.originate.dto

import com.originate.support.BaseSpec

import play.api.libs.json.{JsNull, Json}
import play.api.mvc.Results

class ApiResponseSpec extends BaseSpec with Results {

  case class Data(a: String, b: Int)

  object Data {
    implicit val writes = Json.writes[Data]
  }

  "toJson" - {

    "response is ok" - {

      "sets 'status' to 'ok'" in {
        ApiResponse(Ok, Seq.empty).toJson shouldBe Json.obj(
          "status" -> "ok",
          "message" -> JsNull,
          "data" -> Json.arr()
        )
      }

    }

    "response is not ok" - {

      "sets 'status' to 'error'" in {
        ApiResponse(BadRequest, Seq.empty).toJson shouldBe Json.obj(
          "status" -> "error",
          "message" -> JsNull,
          "data" -> Json.arr()
        )
      }

    }

  }

}
