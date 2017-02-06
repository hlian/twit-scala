package com.originate.support

import com.originate.{AppComponents, MacwireApplicationLoader}

import anorm.SQL
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Environment, Mode}
import play.api.ApplicationLoader.Context
import play.api.db.Databases
import play.api.db.evolutions.Evolutions
import play.api.test.TestServer

class TestApplicationLoader extends MacwireApplicationLoader with ApplicationLoader {
  override def loadRegistry(context: Context) =
    new BuiltInComponentsFromContext(context) with AppComponents {
      override lazy val database = Databases.inMemory()
    }
}

abstract trait IntegrationSpecLike extends BaseSpecLike {

  System.setProperty("config.resource", "integration.conf")

  private val StatsDPort = 18125
  private val statsdServer = new MockStatsDServer(StatsDPort)

  private val env = new Environment(
    new java.io.File("."),
    ApplicationLoader.getClass.getClassLoader,
    Mode.Test
  )
  private val context = ApplicationLoader.createContext(env)
  private val loader = new TestApplicationLoader
  val registry = loader.loadRegistry(context)

  implicit lazy val app = registry.application

  val port = 19001
  val server = TestServer(port, app)
  server.start()

  SQL("SET MODE PostgreSQL").execute()(registry.connection)
  Evolutions.applyEvolutions(registry.database, new EvolutionTransformingReader())

  def afterAll(): Unit = {
    registry.database.shutdown()

  }

}

abstract class IntegrationSpec extends BaseSpec with IntegrationSpecLike
