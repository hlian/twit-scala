package com.originate.util

import org.joda.time.LocalDate
import play.api.mvc.QueryStringBindable

import scala.util.{Either, Failure, Left, Right, Success, Try}

/**
 * Includes helpers to bind types out of query strings. Automatically imported into
 * the generated routes file via `routesImport` in `build.sbt`
 */
object QueryStringBinders {

  implicit val localDateBindable: SimpleBinder[LocalDate] = new SimpleBinder[LocalDate](s => Try(new LocalDate(s)))

  class SimpleBinder[T](f: String => Try[T]) extends QueryStringBindable[T] {
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, T]] =
      params get key flatMap (_.headOption) map { string =>
        f(string) match {
          case Success(t) => Right(t)
          case Failure(e) => Left(e.getMessage)
        }
      }

    def unbind(key: String, t: T): String = t.toString
  }

}
