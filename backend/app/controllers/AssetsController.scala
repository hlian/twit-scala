package com.originate.controllers

import _root_.controllers.Assets
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

class AssetsController(assets: Assets)(implicit ec: ExecutionContext) extends Controller {

  def at(file: String): Action[AnyContent] = assets.at(path = "/public/client", file = "index.html")

}
