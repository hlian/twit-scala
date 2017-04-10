package com.originate.integration.steps

import com.originate.support.BaseSpecLike

import anorm.SqlParser._
import anorm._
import cucumber.api.scala.ScalaDsl
import org.scalatest.selenium.WebBrowser

/**
 * Contains a shared set of tags that can be used to perform setup functionality
 * before a feature or scenario is run
 */
object BeforeTags
  extends ScalaDsl
  with BaseSpecLike
  with WebBrowser
  with StepHelpers {

  private val EvolutionsTable = "play_evolutions"

  Before() { _ =>
    database.withConnection { implicit connection =>
      val tables = SQL(
        """
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = 'PUBLIC'
        """
      ).as(scalar[String].*)

      tables filterNot (_.equalsIgnoreCase(EvolutionsTable)) foreach { tableName =>
        SQL(s"TRUNCATE TABLE $tableName").execute()
      }
    }
  }

  Before("@fresh-browser") { _ =>
    go to path("/")
    deleteAllCookies()
  }

}
