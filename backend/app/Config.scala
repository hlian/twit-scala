package com.originate.config

import com.originate.global.Environment

case class Config(
    applicationName: String,
    environment: Environment,
    auth0: AuthZeroConfig,
    datadog: Datadog
)

case class Datadog(
    agentHost: String,
    agentPort: Int
)

case class AuthZeroConfig(
    domain: String,
    clientId: String,
    clientSecret: String,
    callbackUrl: String
)
