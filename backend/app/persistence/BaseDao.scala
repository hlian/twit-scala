package com.originate.persistence

import com.originate.models.WithId

import anorm.SqlParser._
import anorm._
import play.api.db.Database

import java.sql.Connection
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.blocking

/**
 * BaseDao is a base trait for creating DAOs for models.
 */
abstract class BaseDao[Model](implicit ec: ExecutionContext) {
  type Row = WithId[Model]

  type DataMapper = Seq[(Symbol, Model => ParameterValue)]

  def db: Database

  def execute[T](query: Connection => T): Future[T] =
    Future(blocking(db.withConnection(query)))

  def executeIn[T](ids: Seq[Long])(query: Connection => Seq[T]): Future[Seq[T]] =
    if (ids.isEmpty) Future.successful(Seq.empty)
    else execute(query)

  def executeTransaction[T](query: Connection => T): Future[T] =
    Future(blocking(db.withTransaction(query)))

  def executeTransactionIn[T](ids: Seq[Long])(query: Connection => Seq[T]): Future[Seq[T]] =
    if (ids.isEmpty) Future.successful(Seq.empty)
    else executeTransaction(query)

  protected def enumColumn[T <: Enumeration](enum: T): Column[enum.Value] =
    Column.nonNull { (value: Any, meta) =>
      val MetaDataItem(qualified, _, _) = meta
      value match {
        case i: Int => Right(enum.apply(i))
        case _ =>
          val raw = value.asInstanceOf[AnyRef].getClass // scalastyle:off token
          Left(TypeDoesNotMatch(s"Cannot convert $value: $raw to $enum for column $qualified"))
      }
    }

}
