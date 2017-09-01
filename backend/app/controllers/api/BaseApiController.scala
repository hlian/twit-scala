package com.originate.controllers.api

import com.originate.dto.ApiResponseHelpers
import com.originate.util.Logging

import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Reads
import play.api.mvc.{AbstractController, BodyParser, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Left, Right}

/**
 * BaseApiController should be used as the base trait for all API controllers.
 * Commonly used constructs can be placed within it and easily shared with all
 * controllers from there (especially implicit helpers).
 */
trait BaseApiController extends AbstractController with Logging with ApiResponseHelpers with I18nSupport {

  /**
   * Used to restrict a json endpoint to read a specific type from the request.
   * usage:
   * <pre>
   * {@code
   *   def endpoint = Action.async(json[T]) { implicit request =>
   *     val t: T = request.body
   *     ...
   *   }
   * }
   * </pre>
   */
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
