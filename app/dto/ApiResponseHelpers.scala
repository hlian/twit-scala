package com.originate.dto

import com.originate.util.Ops.AnyOps

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsError, JsPath, Json, Writes}
import play.api.mvc.{Request, RequestHeader, Result, Results}

/**
 * A set of helper methods used to interact with `ApiResponse`
 */
trait ApiResponseHelpers extends Results {

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
