package com.originate.controllers

import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

class PingController()(implicit ec: ExecutionContext) extends Controller {

  def ping(): Action[AnyContent] = Action {
    Ok("pong")
  }

}
