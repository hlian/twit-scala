package com.originate.controllers.api

import com.originate.dto.ApiHelpers
import com.originate.util.Logging

import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Reads
import play.api.mvc.{BodyParser, Controller, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Left, Right}

trait BaseApiController extends Controller with Logging with ApiHelpers with I18nSupport {

  def json[T : Reads](implicit ec: ExecutionContext): BodyParser[T] =
    parse.json validate { json =>
      json.validate[T].fold(
        errors => Left(apiError(errors)): Either[Result, T],
        t => Right(t): Either[Result, T]
      )
    }

  def ifFound[T](modelOption: Option[T])(f: T => Result)(implicit request: RequestHeader): Result =
    modelOption map f getOrElse apiNotFound(Messages("error.notFound"))

  def ifFoundAsync[T](modelOption: Option[T])(f: T => Future[Result])(implicit request: RequestHeader): Future[Result] =
    modelOption map f getOrElse Future.successful(apiNotFound(Messages("error.notFound")))

}
