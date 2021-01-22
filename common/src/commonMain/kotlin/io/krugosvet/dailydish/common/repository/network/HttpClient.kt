package io.krugosvet.dailydish.common.repository.network

import io.krugosvet.dailydish.common.core.jsonConfig
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType

internal fun createHttpClient(): HttpClient =
  HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer(jsonConfig)
    }

    install(HttpTimeout) {
      requestTimeoutMillis = TIMEOUT
      connectTimeoutMillis = TIMEOUT
      socketTimeoutMillis = TIMEOUT
    }

    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.INFO
    }

    defaultRequest {
      // FIXME
      // parameter("api_key", "some_api_key")

      accept(ContentType.Application.Json)
    }
  }

private const val TIMEOUT = 150000L
