package com.originate.dto

import com.originate.support.BaseSpec

class PagingDataSpec extends BaseSpec {

  val Limit = PagingData.DefaultLimit
  val pagingData = PagingData(Limit, Some(100))

  "next" - {

    "supplies paging data for the next page" in {
      pagingData.next shouldBe PagingData(Limit, Some(120))
    }

  }

  "previous" - {

    "supplies paging data for the previous page" in {
      pagingData.previous shouldBe PagingData(Limit, Some(80))
    }

    "when page is within limit from the beginning" - {

      "sets the previous offset to 0" in {
        val beginningPagingData = PagingData(Limit, Some(10))
        beginningPagingData.previous shouldBe PagingData(Limit, Some(0))
      }

    }

  }

  "apply" - {

    "enforces a maximum" in {
      PagingData(PagingData.MaxLimit + 1).limit shouldBe PagingData.MaxLimit
    }

    "with an offset" - {

      "uses that offset" in {
        PagingData(Limit, Some(100)).offset shouldBe 100
      }

      "sets the previous offset to be (offset - limit)" in {
        PagingData(Limit, Some(100)).previous.offset shouldBe (100 - Limit)
      }

      "sets the next offset to be (offset + limit)" in {
        PagingData(Limit, Some(100)).next.offset shouldBe (100 + Limit)
      }
    }
  }

  "query binding/unbinding" - {

    "bind" - {

      def bind(args: (String, Int)*): PagingData =
        PagingData.queryStringBinder.bind("", args.toMap.mapValues(i => Seq(i.toString))).get.right.get

      "without any query params" - {

        "sets default values for paging data" in {
          val pagingData = bind()

          pagingData.limit shouldBe PagingData.DefaultLimit
          pagingData.offset shouldBe 0
        }

      }

      "with query params" - {

        "sets the values for paging data" in {
          val pagingData = bind("limit" -> 10, "offset" -> 1)

          pagingData.limit shouldBe 10
          pagingData.offset shouldBe 1
        }

      }

    }

    "unbind" - {

      "builds a valid query string" in {
        PagingData.queryStringBinder.unbind("", PagingData(10)) shouldBe "limit=10&offset=0"
      }

    }

  }

}
