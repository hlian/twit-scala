package com.originate.support

import com.originate.{AppComponents, MacwireApplicationLoader}

import anorm.SQL
import org.scalatest.BeforeAndAfterAll
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Environment, Mode}
import play.api.ApplicationLoader.Context
import play.api.db.Databases
import play.api.db.evolutions.Evolutions
import play.api.test.TestServer

class TestApplicationLoader extends MacwireApplicationLoader with ApplicationLoader {
  override def loadRegistry(context: Context) =
    new BuiltInComponentsFromContext(context) with AppComponents {
      override lazy val database = Databases.inMemory()
      database.withConnection { implicit connection =>
        SQL("SET MODE PostgreSQL").execute()
        Evolutions.applyEvolutions(database, new EvolutionTransformingReader())
      }
    }
}

abstract trait IntegrationSpecLike extends BaseSpecLike {
  System.setProperty("config.resource", "integration.conf")

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
}

abstract class IntegrationSpec extends BaseSpec with IntegrationSpecLike with BeforeAndAfterAll {
  private val StatsDPort = 18125
  private val statsdServer = new MockStatsDServer(StatsDPort)

  override def afterAll(): Unit = {
    registry.database.shutdown()
    statsdServer.close()
  }
}
