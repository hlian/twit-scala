package com.originate.util

import com.originate.support.BaseSpec

import play.api.mvc.AnyContentAsEmpty
import play.api.test.{FakeHeaders, FakeRequest}

class URIBuilderSpec extends BaseSpec {

  val path = "/path/to/thing"
  val addedParams = "&limit=50&previousOffset=0&nextOffset=50"
  val data = Map(
    "limit" -> Seq("50"),
    "previousOffset" -> Seq("0"),
    "nextOffset" -> Seq("50")
  )

  def fakeRequest(queryString: String = ""): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", s"$path?$queryString", FakeHeaders(), AnyContentAsEmpty)

  def build(queryString: String): String =
    URIBuilder.encodeQuery(fakeRequest(queryString).queryString ++ data)

  "addQueryToRequestPath" - {

    "returns a path with query string added" in {
      URIBuilder.addQueryToRequestPath(fakeRequest("test=1"), data) shouldBe s"$path?test=1$addedParams"
    }

  }

  "encodeQuery" - {

    "retains existing query params and adds new params" in {
      build("test=1") shouldBe s"test=1$addedParams"
    }

    "retains nested query params and adds new params" in {
      build("test.some=1") shouldBe {
        s"test.some=1$addedParams"
      }
    }

    "retains url encoded params and adds new params" in {
      build("test+key=test+value") shouldBe {
        s"test+key=test+value$addedParams"
      }
      build("testékey=testévalue") shouldBe { // scalastyle:off non.ascii.character.disallowed
        s"test%C3%A9key=test%C3%A9value$addedParams"
      }
    }

    "retains empty params and adds new params" in {
      build("test=") shouldBe s"test=$addedParams"
    }

    "list like params" - {

      "retains list-like query params and adds new params" in {
        build("test[]=1&test[]=2") shouldBe {
          s"test%5B%5D=1&test%5B%5D=2$addedParams"
        }
      }

      "retains list-like query params that are encoded and adds new params" in {
        build("test%5B%5D=1&test%5B%5D=2") shouldBe {
          s"test%5B%5D=1&test%5B%5D=2$addedParams"
        }
      }

    }

  }

}
