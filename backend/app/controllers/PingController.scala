package com.originate.controllers

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json.{Format, Json}

import com.originate.dto.ApiResponseHelpers

case class PingResponse(status: String)

object PingResponse {
  implicit val format: Format[PingResponse] = Json.format[PingResponse]
}

class PingController(cc: ControllerComponents) extends AbstractController(cc) with ApiResponseHelpers {

  def ping(): Action[AnyContent] = Action {
    apiOk(PingResponse("all good!"))
  }

}
