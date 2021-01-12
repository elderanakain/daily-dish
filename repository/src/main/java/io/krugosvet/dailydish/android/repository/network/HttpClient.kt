package io.krugosvet.dailydish.android.repository.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

private const val TIMEOUT = 150000L

fun createHttpClient(): HttpClient =
  HttpClient(OkHttp) {
    install(JsonFeature) {
      val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
      }

      serializer = KotlinxSerializer(json)
    }

    install(HttpTimeout) {
      requestTimeoutMillis = TIMEOUT
      connectTimeoutMillis = TIMEOUT
      socketTimeoutMillis = TIMEOUT
    }

    defaultRequest {
      // FIXME
      // parameter("api_key", "some_api_key")

      accept(ContentType.Application.Json)
    }

    engine {
      addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
    }
  }
