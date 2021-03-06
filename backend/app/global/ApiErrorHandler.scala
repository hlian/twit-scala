package com.originate.global

import com.originate.dto.ApiResponseHelpers
import com.originate.global.exceptions.{ExternalServiceException, NoPermissionException, NotFoundException}
import com.originate.util.Logging

import play.api.http.HttpErrorHandler
import play.api.http.Status.{FORBIDDEN, INTERNAL_SERVER_ERROR, NOT_FOUND, SERVICE_UNAVAILABLE}
import play.api.mvc.{RequestHeader, Result, Results}

import java.io.{PrintWriter, StringWriter}
import scala.concurrent.Future

/**
 * ApiErrorHandler is hooked into the application in MacwireApplicationLoader and serves as the
 * final step that exceptions pass through before a request is sent back to the user. Exceptions
 * from all parts of the application can be extended with types to help formulate the appropriate
 * error message to be returned to the user (by passing through `exceptionToResponse`).
 */
class ApiErrorHandler extends HttpErrorHandler with ApiResponseHelpers with Results with Logging {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    logger.warn(s"client error on path: ${request.path}, with message: $message")

    Future.successful(apiError(statusCode, "A client error occurred: " + message))
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    Future.successful {
      logException(request.path, exception)

      val (status, message) = exceptionToResponse(exception)

      apiError(status, "A server error occurred: " + message)
    }

  private val exceptionToResponse: PartialFunction[Throwable, (Int, String)] = {
    case e: NoPermissionException => (FORBIDDEN, e.getMessage)
    case e: NotFoundException => (NOT_FOUND, e.getMessage)
    case e: ExternalServiceException => (SERVICE_UNAVAILABLE, e.getMessage)
    case e: Any => (INTERNAL_SERVER_ERROR, e.getMessage)
  }

  private def logException(path: String, exception: Throwable): Unit = {
    val stringWriter = new StringWriter
    val printWriter = new PrintWriter(stringWriter)
    exception.printStackTrace(printWriter)

    logger.error(s"server error on path: $path, with stack trace: $stringWriter")
  }

}
