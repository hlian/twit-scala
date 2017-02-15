package com.originate.dto

import com.originate.support.BaseSpec

import play.api.mvc.{AnyContentAsEmpty, Results}
import play.api.test.{FakeHeaders, FakeRequest}

class ApiResponsePagedSpec extends BaseSpec with Results {

  "toJson" - {

    val path = "/path/to/thing"
    val pagingData = PagingData(50, Some(10))
    val apiResponse = ApiResponse(Ok, Seq.empty)
    val response = ApiResponsePaged(apiResponse, pagingData)

    def fakeRequest(query: String = ""): FakeRequest[AnyContentAsEmpty.type] =
      FakeRequest("GET", s"$path?$query", FakeHeaders(), AnyContentAsEmpty)

    def extract(response: ApiResponsePaged, key: String, query: String = ""): String =
      response.toJson(fakeRequest(query)).value(key).as[String]

    "adds query params to the path" in {
      extract(response, "previous") shouldBe s"$path?limit=50&offset=0"
      extract(response, "next") shouldBe s"$path?limit=50&offset=60"
    }

    "overwrites existing paging data in that path" in {
      extract(response, "previous", "offset=10") shouldBe {
        s"$path?limit=50&offset=0"
      }
      extract(response, "next", "offset=10") shouldBe {
        s"$path?limit=50&offset=60"
      }
    }

  }

}
