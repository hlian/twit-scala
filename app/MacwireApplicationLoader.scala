package com.originate

import _root_.controllers.Assets
import com.softwaremill.macwire._
import play.api.{Application, ApplicationLoader, BuiltInComponents, BuiltInComponentsFromContext}
import play.api.ApplicationLoader.Context
import play.api.db.evolutions.EvolutionsComponents
import play.api.routing.Router
import router.Routes

/**
 * MacwireApplicationLoader replaces the default Play application loader with a
 * compile time DI system (using macwire).
 *
 * More info: https://www.playframework.com/documentation/2.5.x/ScalaCompileTimeDependencyInjection
 */
class MacwireApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = loadRegistry(context).application

  def loadRegistry(context: Context): AppComponents =
    new BuiltInComponentsFromContext(context)
      with AppComponents
      with ApplyEvolutions
}

trait AppComponents
  extends BuiltInComponents
  with Registry {

  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }
}

trait ApplyEvolutions extends EvolutionsComponents {
  applicationEvolutions
}
