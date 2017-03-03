package com.originate.integration.steps

class PingSteps extends BaseSteps with StepHelpers {

  When("""^I go to the ping endpoint$""") { () =>
    go to path("/ping")
  }

  Then("""^I get a pong response$""") { () =>
    eventually {
      find("body").map(_.text) contains "pong"
    }
  }
}
