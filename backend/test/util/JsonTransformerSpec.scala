package com.originate.util

import com.originate.support.BaseSpec

import play.api.libs.json.Json

class JsonTransformerSpec extends BaseSpec {

  val json = Json.obj(
    "branch1" -> Json.arr(
      Json.obj("branch11" -> Json.obj(
        "find" -> "me",
        "other" -> "here"
      )),
      Json.obj("branch12" -> Json.obj(
        "find" -> "metoo"
      )),
      Json.obj("branch13" -> Json.obj(
        "dont" -> "findme"
      ))
    ),
    "branch2" -> Json.obj(
      "find" -> "me"
    )
  )

  "pruneObject" - {

    "when matcher matches" - {

      val Some(result) = JsonTransformer.pruneObject(json) { json =>
        (json \ "find").asOpt[String] exists (_.startsWith("me"))
      }

      "removes all matching objects" in {
        (result \\ "branch2") shouldBe empty
        (result \\ "branch11") shouldBe empty
        (result \\ "branch12") shouldBe empty
      }

      "keeps non matching objects" in {
        (result \\ "branch13") should not be empty
      }

      "retains values" in {
        ((result \\ "branch13")(0) \ "dont").as[String] shouldBe "findme"
      }

    }

    "when matcher doesn't match" - {

      val Some(result) = JsonTransformer.pruneObject(json)(_ => false)

      "does not alter the object" in {
        result shouldBe json
      }

    }

  }

}
