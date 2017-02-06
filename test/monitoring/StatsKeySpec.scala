package com.originate.monitoring

import com.originate.support.BaseSpec

class StatsKeySpec extends BaseSpec {

  "StatsKey Companion" - {

    "isValid" - {

      "is true for keys containing only a-z, _, and ." in {
        StatsKey.isValid(StatsKey("some_key")) shouldBe true
        StatsKey.isValid(StatsKey("some.key")) shouldBe true
        StatsKey.isValid(StatsKey("some.key_10")) shouldBe true
        StatsKey.isValid(StatsKey("some_key.here")) shouldBe true
      }

      "is false for keys containing characters other than a-z, _, and ." in {
        StatsKey.isValid(StatsKey("some_key:here")) shouldBe false
        StatsKey.isValid(StatsKey("some.key name")) shouldBe false
        StatsKey.isValid(StatsKey("some|key")) shouldBe false
        StatsKey.isValid(StatsKey("")) shouldBe false
      }

    }

    "normalize" - {

      "is lowercase" in {
        StatsKey.normalize(StatsKey("some.Key")) shouldBe "some.key"
      }

      "is snake case" in {
        StatsKey.normalize(StatsKey("some.KeyName")) shouldBe "some.key_name"
        StatsKey.normalize(StatsKey("some.keyName")) shouldBe "some.key_name"
      }

      "is snake case for abbreviated characters" in {
        StatsKey.normalize(StatsKey("some.KeyDName")) shouldBe "some.key_d_name"
        StatsKey.normalize(StatsKey("some.keyDName")) shouldBe "some.key_d_name"
      }

      "handles oddly formatted keys" in {
        StatsKey.normalize(StatsKey("some.keyName.EventName.mix_of_SnakeCase")) shouldBe
          "some.key_name.event_name.mix_of_snake_case"
      }

    }

  }

}
