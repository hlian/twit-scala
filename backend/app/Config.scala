package com.originate.config

import com.originate.global.Environment

case class Config(
    applicationName: String,
    environment: Environment,
    datadog: Datadog
)

case class Datadog(
    agentHost: String,
    agentPort: Int
)
