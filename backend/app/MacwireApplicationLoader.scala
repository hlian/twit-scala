package com.originate

import com.originate.filters.{DatadogFilter, LoggingFilter}
import com.originate.global.ApiErrorHandler

import _root_.controllers.AssetsComponents
import com.softwaremill.macwire._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.ApplicationLoader.Context
import play.api.db.{DBComponents, Database}
import play.api.db.evolutions.Evolutions
import play.api.routing.Router
import pureconfig._
import router.Routes

/**
 * MacwireApplicationLoader replaces the default Play application loader with a
 * compile time DI system (using macwire).
 *
 * More info: https://www.playframework.com/documentation/2.6.x/ScalaCompileTimeDependencyInjection
 */
class MacwireApplicationLoader extends ComponentsLoader {

  def loadComponents(context: Context): Components =
    new Components(context) with DefaultDatabase

}

abstract class ComponentsLoader extends ApplicationLoader {

  def loadComponents(context: Context): Components

  def load(context: Context): Application = {
    val registry = loadComponents(context)
    // While this could be in a class definition, it helps to have it
    // run in a method so you have exact control over when it runs.
    Evolutions.applyEvolutions(registry.database)
    registry.application
  }

}

abstract class Components(context: Context)
  extends BuiltInComponentsFromContext(context)
    with AssetsComponents
    with Registry {

  // Abstract database, to be overriden for tests
  val database: Database

  // See https://www.playframework.com/documentation/2.6.x/ScalaCompileTimeDependencyInjection#Configuring-Logging
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  override lazy val httpFilters = Seq(
    wire[LoggingFilter],
    wire[DatadogFilter]
  )

  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }

}

trait DefaultDatabase extends DBComponents {

  val database = dbApi.database("default")

}
