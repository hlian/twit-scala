package com.originate.support

import com.originate.MockRegistry

import anorm.SQL
import org.scalatest.BeforeAndAfterAll
import play.api.db.Databases
import play.api.db.evolutions.Evolutions

/**
 * DBFunctionalSpec is a base trait for running unit tests on DAOs. It uses
 * EvolutionTransformingReader to translate any DB specific SQL to the equivalent H2 syntax.
 */
abstract class DBFunctionalSpec extends BaseSpec with MockRegistry with BeforeAndAfterAll {

  val database = Databases.inMemory()
  implicit val realConnection = database.getConnection()

  SQL("SET MODE PostgreSQL").execute()
  Evolutions.applyEvolutions(database, new EvolutionTransformingReader())

  def ignoreForeignKeys(): Unit =
    SQL("SET REFERENTIAL_INTEGRITY FALSE").execute()

  override def afterAll(): Unit = {
    database.shutdown()
  }

}
