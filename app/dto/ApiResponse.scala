package com.originate.dto

import com.originate.util.Ops.AnyOps
import com.originate.util.URIBuilder

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsError, JsObject, JsPath, JsValue, Json, Writes}
import play.api.mvc.{Request, RequestHeader, Result, Results}

/**
 * ApiResponse is a wrapper around data used to ensure a consistent Json structure
 * returned by the API. The main benefit is that it gives flexibility as the API evolves
 * so that you can add more information to responses without breaking clients.
 *
 * For example: when adding paging to your API, paging info can be added to the top level Json
 * object without breaking any clients that have not been updated.
 */
case class ApiResponse(
    result: Results#Status,
    data: Seq[JsValue],
    message: Option[String] = None) extends Results {

  private val statusString = if (result.header === Ok.header) "ok" else "error"

  def toResult: Result =
    result(this.toJson)

  def toJson: JsObject =
    Json.obj(
      "status" -> statusString,
      "message" -> message,
      "data" -> data
    )

}

case class ApiResponsePaged(apiResponse: ApiResponse, pagingData: PagingData) {

  def toResult(implicit request: RequestHeader): Result =
    apiResponse.result(this.toJson)

  def toJson(implicit request: RequestHeader): JsObject =
    apiResponse.toJson ++ Json.obj(
      "previous" -> URIBuilder.addQueryToRequestPath(request, pagingData.previous.toQueryKeys),
      "next" -> URIBuilder.addQueryToRequestPath(request, pagingData.next.toQueryKeys)
    )

}

object ApiResponse extends Results {
  def apply(result: Results#Status, data: Seq[JsValue], message: String): ApiResponse =
    ApiResponse(result, data, Some(message))
}

trait ApiHelpers extends Results {

  def apiOk(): Result =
    ApiResponse(Ok, Seq.empty).toResult

  def apiOkMessage(message: String): Result =
    ApiResponse(Ok, Seq.empty, message).toResult

  def apiOkSeq[T : Writes](data: Seq[T]): Result =
    ApiResponse(Ok, data map (Json.toJson(_))).toResult

  def apiOk[T : Writes](data: T*): Result =
    ApiResponse(Ok, data map (Json.toJson(_))).toResult

  def apiOkSeqPaged[T : Writes](pagingData: PagingData, data: Seq[T])(implicit request: RequestHeader): Result =
    ApiResponsePaged(
      ApiResponse(Ok, data map (Json.toJson(_))),
      pagingData
    ).toResult

  def apiOkPaged[T : Writes](pagingData: PagingData, data: T*)(implicit request: RequestHeader): Result =
    ApiResponsePaged(
      ApiResponse(Ok, data map (Json.toJson(_))),
      pagingData
    ).toResult

  def apiError(errors: Seq[(JsPath, Seq[ValidationError])]): Result =
    ApiResponse(BadRequest, Seq(JsError.toJson(errors))).toResult

  def apiError(jsError: JsError): Result =
    ApiResponse(BadRequest, Seq(JsError.toJson(jsError))).toResult

  def apiError(message: String): Result =
    ApiResponse(BadRequest, Seq.empty, message).toResult

  def apiError(status: Results#Status, message: String): Result =
    ApiResponse(status, Seq.empty, message).toResult

  def apiError(status: Int, message: String): Result =
    ApiResponse(Status(status), Seq.empty, message).toResult

  def apiError[T : Writes](status: Int, data: T*): Result =
    ApiResponse(Status(status), data map (Json.toJson(_))).toResult

  def apiNotFound(message: String): Result =
    ApiResponse(NotFound, Seq.empty, message).toResult

  def apiNotFound[Id](id: Id, model: String): Result =
    ApiResponse(NotFound, Seq.empty, s"$model with id: $id does not exist").toResult

  def apiForbidden[T]()(implicit request: Request[T]): Result =
    apiForbidden(request.path)

  def apiForbidden(url: String): Result =
    ApiResponse(Forbidden, Seq.empty, s"you do not have permission to access ${url}").toResult

}
