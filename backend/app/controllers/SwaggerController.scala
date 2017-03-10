package com.originate.controllers

import com.originate.util.JsonTransformer

import com.iheart.playSwagger.SwaggerSpecGenerator
import play.api.libs.json.{JsObject, JsValue}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}

class SwaggerController(implicit ec: ExecutionContext) extends Controller {

  implicit val cl: ClassLoader = getClass.getClassLoader

  private lazy val autoGeneratedDefinitions = SwaggerSpecGenerator(
    "com.originate.models",
    "com.originate.dto"
  )

  def redirectToSwagger: Action[AnyContent] = Action {
    Redirect("/api/docs/swagger-ui/index.html?url=/api/docs/swagger.json#/routes")
  }

  def specs: Action[AnyContent] = Action.async {
    Future.fromTry(autoGeneratedDefinitions.generate()) map { generatedSwaggerJson =>
      Ok(transformSwaggerOutput(generatedSwaggerJson))
    }
  }

  private def transformSwaggerOutput(json: JsValue): JsValue =
    JsonTransformer.pruneObject(json)(objectContainsHiddenKey) getOrElse json

  private def objectContainsHiddenKey(json: JsObject): Boolean =
    (json \ "hidden").asOpt[Boolean] contains true

}
