package com.originate.dto

import com.originate.util.Ops.AnyOps
import com.originate.util.URIBuilder

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{RequestHeader, Result, Results}

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
