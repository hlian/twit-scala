package com.originate.persistence

import com.originate.models.WithId

import anorm.SqlParser._
import anorm._

import java.sql.Connection
import scala.concurrent.{ExecutionContext, Future}

/**
 * BaseDao is a base trait for creating DAOs for models.
 */
abstract class BaseDao[Model](implicit ec: ExecutionContext, connection: Connection) {
  type Row = WithId[Model]

  type DataMapper = Seq[(Symbol, Model => ParameterValue)]

  def execute[T](query: => T): Future[T] = Future(query)

  def executeIn[T](ids: Seq[Long])(query: => Seq[T]): Future[Seq[T]] =
    if (ids.isEmpty) Future.successful(Seq.empty)
    else execute(query)

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
