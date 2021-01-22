package io.krugosvet.dailydish.common.core

import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger { }

public fun <T> Result<T>.getOrThrowWithLog(): T =
  try {
    getOrThrow()
  } catch (e: Throwable) {
    logger.error { e }
    throw e
  }
