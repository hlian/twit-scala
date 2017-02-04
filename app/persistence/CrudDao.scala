package com.originate.persistence

import com.originate.global.exceptions.{ModelCannotBePersisted, ModelNotFound}
import com.originate.models.WithId

import anorm.SqlParser._
import anorm._

import java.sql.Connection
import scala.concurrent.{ExecutionContext, Future}

/**
 * CrudDao is a base trait for providing standard CRUD functionality to a DAO for a model.
 */
abstract class CrudDao[Model](
    val tableName: String)
    (implicit ec: ExecutionContext,
    connection: Connection)
  extends BaseDao[Model] {

  protected val IdFieldSymbol = 'id

  protected val IdFieldString = "id"

  protected def parser: RowParser[Model]

  protected def dataMapper: DataMapper

  protected def rowParser: RowParser[Row] =
    get[Int](IdFieldString) ~ parser map {
      case id ~ model => WithId(id, model)
    }

  protected def idField(row: Row): NamedParameter =
    idField(row.id)

  protected def idField(id: Int): NamedParameter =
    (IdFieldSymbol, id)

  protected def fullSplat(row: Row): Seq[NamedParameter] =
    idField(row) +: dataSplat(row.model)

  protected def dataFieldString: String =
    dataFields map (_.name) mkString ", "

  protected def dataFieldInsertString: String =
    dataFields map (s => s"{${s.name}}") mkString ", "

  protected def dataFieldSetString: String =
    dataFields map (s => s"${s.name} = {${s.name}}") mkString ", "

  protected def fullFieldString: String =
    (IdFieldSymbol +: dataFields) map (_.name) mkString ", "

  protected def absoluteFullFieldString: String =
    (IdFieldSymbol +: dataFields) map (tableName + "." + _.name) mkString ", "

  private def dataFields: Seq[Symbol] =
    dataMapper.unzip._1

  private def dataSplat(model: Model): Seq[NamedParameter] =
    dataMapper map {
      case (symbol, f) => NamedParameter(symbol.name, f(model))
    }

  def getAll(): Future[Seq[Row]] = execute {
    SQL(s"SELECT $fullFieldString FROM $tableName ORDER BY $IdFieldString DESC")
      .as(rowParser.*)
  }

  def count(): Future[Long] = execute {
    SQL(s"SELECT COUNT(1) as count FROM $tableName")
      .as(scalar[Long].single)
  }

  def findByIdOrFail(id: Int): Future[Row] =
    findById(id) map {
      _ getOrElse (throw new ModelNotFound(tableName, id))
    }

  def findById(id: Int): Future[Option[Row]] = execute {
    SQL(
      s"""
      SELECT $fullFieldString
      FROM $tableName
      WHERE $IdFieldString = {${IdFieldSymbol.name}}
      LIMIT 1
      """
    ).on(idField(id))
    .as(rowParser.*)
    .headOption
  }

  def findByRelation(field: Symbol, id: Int): Future[Seq[Row]] = execute {
    SQL(
      s"""
      SELECT $fullFieldString
      FROM $tableName
      WHERE ${field.name} = {id}
      ORDER BY $IdFieldString DESC
      """
    ).on('id -> id)
    .as(rowParser.*)
  }

  def findOneByField[T](field: Symbol, value: T)(implicit f: T => ParameterValue): Future[Option[Row]] = execute {
    SQL(
      s"""
      SELECT *
      FROM $tableName
      WHERE ${field.name} = {value}
      LIMIT 1
      """
    ).on('value -> value)
    .as(rowParser.*)
    .headOption
  }

  def getAllIfCondition(condition: Boolean, field: Symbol): Future[Seq[Row]] = execute {
    SQL(
      s"""
      SELECT $fullFieldString
      FROM $tableName
      WHERE ${field.name} = {condition}
      ORDER BY $IdFieldString DESC
      """
    ).on('condition -> condition)
    .as(rowParser.*)
  }

  def remove(row: Row): Future[Unit] = remove(row.id)

  def remove(model: Int): Future[Unit] = execute {
    SQL(s"DELETE FROM $tableName WHERE $IdFieldString = {$IdFieldString}")
      .on(idField(model))
      .executeUpdate()
  } map (_ => ())

  def create(model: Model): Future[Row] = execute {
    val returnedIds = SQL(
      s"""
        INSERT INTO $tableName ($dataFieldString)
        VALUES ($dataFieldInsertString)
        """
      ).on(dataSplat(model): _*)
      .executeInsert()

    returnedIds map (l => WithId(l.toInt, model)) getOrElse (throw new ModelCannotBePersisted(tableName, model))
  }

  def update(row: Row): Future[Row] = execute {
    SQL(
      s"""
      UPDATE $tableName
      SET $dataFieldSetString
      WHERE $IdFieldString = {$IdFieldString}
      """
    ).on(fullSplat(row): _*)
    .executeUpdate()

    row
  }

}
