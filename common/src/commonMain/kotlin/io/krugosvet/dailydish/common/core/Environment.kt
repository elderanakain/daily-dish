package io.krugosvet.dailydish.common.core

internal enum class Environment(
  val endpoint: String
) {
  DEV("https://daily-dish-be-staging.herokuapp.com"),
  STAGING("https://daily-dish-be-staging.herokuapp.com"),
  PROD("https://daily-dish-be.herokuapp.com");
}

internal expect val environment: Environment
