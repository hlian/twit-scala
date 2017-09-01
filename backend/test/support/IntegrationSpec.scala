package com.originate.support

import com.originate.{Components, ComponentsLoader}

import anorm.SQL
import org.scalatest.BeforeAndAfterAll
import play.api.{ApplicationLoader, Environment, Mode}
import play.api.ApplicationLoader.Context
import play.api.db.Databases
import play.api.db.evolutions.Evolutions
import play.api.test.TestServer

class TestApplicationLoader extends ComponentsLoader {

  val inMemoryDatabase = {
    val db = Databases.inMemory()
    db.withConnection { implicit connection =>
      SQL("SET MODE PostgreSQL").execute()
      Evolutions.applyEvolutions(db, new EvolutionTransformingReader())
    }
    db
  }

  override def loadComponents(context: Context): Components =
    new Components(context) {
      val database = inMemoryDatabase
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
  val components = loader.loadComponents(context)

  implicit lazy val app = components.application

  val port = 19001
  val server = TestServer(port, app)
  server.start()

}

abstract class IntegrationSpec extends BaseSpec with IntegrationSpecLike with BeforeAndAfterAll {

  private val StatsDPort = 18125
  private val statsdServer = new MockStatsDServer(StatsDPort)

  override def afterAll(): Unit = {
    components.database.shutdown()
    statsdServer.close()
  }

}
