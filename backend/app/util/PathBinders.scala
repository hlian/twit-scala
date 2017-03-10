package com.originate.util

import org.joda.time.LocalDate
import play.api.mvc.PathBindable

import scala.util.{Either, Failure, Left, Right, Success, Try}

/**
 * Includes helpers to bind types out of URL paths. Automatically imported into
 * the generated routes file via `routesImport` in `build.sbt`
 */
object PathBinders {

  implicit val localDatePathBinder: SimplePathBinder[LocalDate] =
    new SimplePathBinder[LocalDate](s => Try(new LocalDate(s)))

  class SimplePathBinder[T](f: String => Try[T]) extends PathBindable[T] {
    def bind(key: String, value: String): Either[String, T] =
      f(value) match {
        case Success(t) => Right(t)
        case Failure(e) => Left(e.getMessage)
      }

    def unbind(key: String, t: T): String = t.toString
  }

}
