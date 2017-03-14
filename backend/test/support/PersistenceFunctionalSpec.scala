package com.originate.support

import com.originate.MockRegistry

import anorm.SQL
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.db.{Database, Databases}
import play.api.db.evolutions.{Evolutions, InconsistentDatabase}

import java.sql.Connection

/**
 * PersistenceFunctionalSpec is a base trait for running unit tests on DAOs. It provides
 * the required structure to stand up, evolve, clean up, and teardown a database connection
 * used for testing.
 */
trait PersistenceFunctionalSpec
  extends BaseSpec
  with MockRegistry
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  def database: Database

  def realConnection: Connection

  def setupTest(): Unit

  def cleanupTest(): Unit

  def startup(): Unit = ()

  def shutdown(): Unit = ()

  def ignoreForeignKeys(): Unit = SQL("SET REFERENTIAL_INTEGRITY FALSE").execute()

  override def beforeEach(): Unit =
    try setupTest()
    catch {
      case e: InconsistentDatabase =>
        throw new Exception(s"Unable to apply evolutions: ${e.error}", e)
    }

  override def afterEach(): Unit = cleanupTest()

  override def beforeAll(): Unit = startup()

  override def afterAll(): Unit = {
    shutdown()
    database.shutdown()
  }

}

/**
 * H2FunctionalSpec runs your specs against an in memory Postgres-ish like database.
 * Tests run against H2 will be about 2x faster but you might run into compatibility
 * issues (H2 doesn't support the full PG spec). EvolutionTransformingReader is included
 * to help mitigate this by allowing you to transform your evolutions before running them.
 */
trait H2FunctionalSpec extends PersistenceFunctionalSpec {
  val database = Databases.inMemory()

  implicit val realConnection = database.getConnection()

  override def startup(): Unit = SQL("SET MODE PostgreSQL").execute()

  def setupTest(): Unit = Evolutions.applyEvolutions(database, new EvolutionTransformingReader())

  def cleanupTest(): Unit = Evolutions.cleanupEvolutions(database)
}

/**
 * DBFunctionalSpec is a shared trait between the functional spec traits that hit a real
 * database
 */
trait DBFunctionalSpec extends PersistenceFunctionalSpec {
  val dbConfig = config.db.default

  def setupTest(): Unit = Evolutions.applyEvolutions(database)

  def cleanupTest(): Unit = Evolutions.cleanupEvolutions(database)
}

/**
 * PgFunctionalSpec provides an implicit connection to the test database set up via docker-compose
 */
trait PgFunctionalSpec extends DBFunctionalSpec {
  val userInfo = s"${dbConfig.dbUsername}:${dbConfig.dbPassword}"
  val hostAndDatabase = s"${dbConfig.host}:${dbConfig.port}${dbConfig.database}"
  val database = Databases(
    driver = "org.postgresql.Driver",
    url = s"postgres://$userInfo@$hostAndDatabase"
  )

  implicit val realConnection = database.getConnection()
}

/**
 * MySQLFunctionalSpec is included in this repo as an example, but not configured out of
 * the box. You will need to add the dependency to build.sbt
 */
trait MySQLFunctionalSpec extends DBFunctionalSpec {
  val database = Databases(
    driver = "com.mysql.jdbc.Driver",
    url = s"jdbc:mysql://localhost${dbConfig.database}",
    config = Map(
      "user" -> dbConfig.dbUsername,
      "password" -> dbConfig.dbPassword
    )
  )

  implicit val realConnection = database.getConnection()
}
