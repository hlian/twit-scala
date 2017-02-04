package com.originate.config

case class Config(
    auth0: AuthZeroConfig
)

case class AuthZeroConfig(
    domain: String,
    clientId: String,
    clientSecret: String,
    callbackUrl: String
)
