package com.originate.controllers

import _root_.controllers.Assets
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

class AssetsController(
    assets: Assets,
    cc: ControllerComponents) extends AbstractController(cc) {

  // For one-page apps, we want to redirect any paths that isn't an
  // asset back to index.html, where the one page is hosted. The
  // frontend code can then render a page-not-found error.
  def catchAll: String => Action[AnyContent] =
    _ => assets.at(path = "/public/client", file = "index.html")

  def client(file: String): Action[AnyContent] =
    assets.at(path = "/public/client/", file = file)

  def swagger(file: String): Action[AnyContent] =
    assets.at(path = "/public/lib/swagger-ui", file = file)

}
