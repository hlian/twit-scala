package com.originate.persistence

import com.originate.models.Example

import anorm.JodaParameterMetaData._
import anorm.SqlParser._
import anorm._
import org.joda.time.DateTime

import java.sql.Connection
import scala.concurrent.ExecutionContext

class ExampleDao(
    implicit ec: ExecutionContext,
    connection: Connection)
  extends CrudDao[Example](Tables.Examples) {

  // parser is what anorm will use to take data from the db and convert it into scala values
  //
  // option 1: build a parser using the built in macro
  //
  // this is ideal for when your model only contains types that don't need special handling:
  //   Integer, String, Boolean, BigDecimal, BigInt, BigInteger, Character, Integer, UUID
  //   also: org.joda.time.{DateTime, LocalDate, LocalDateTime, Instant}
  //
  // note that the field names are only required if they don't map directly (camel <-> snake case)
  val parser = Macro.parser[Example]("name", "created_at")

  // option 2: build a parser by composition
  //
  // this is ideal for any parsers that need to specially handle the types
  // (eg: mapping strings to enumerations)
  //
  // val parser = for {
  //   name <- get[String]("name")
  //   createdAt <- get[DateTime]("created_at")
  // } yield Example(convertToOtherType(name), createdAt)

  // dataMapper is used to convert scala values to something that the database understands.
  // note that the type annotation is required to force the implicit conversion of each value to
  // its SQL equivalent
  //
  // type DataMapper = Seq[(Symbol, Model => ParameterValue)]
  // the implicit conversion to ParameterValue comes from importing anorm._
  val dataMapper: DataMapper = Seq(
    'name -> (_.name),
    'created_at -> (_.createdAt)
  )

}
