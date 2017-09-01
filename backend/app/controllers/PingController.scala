package com.originate.controllers

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class PingController(cc: ControllerComponents) extends AbstractController(cc) {

  def ping(): Action[AnyContent] = Action {
    Ok("pong")
  }

}
