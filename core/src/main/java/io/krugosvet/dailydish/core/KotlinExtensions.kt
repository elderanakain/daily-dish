package io.krugosvet.dailydish.core

import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

fun <T> Result<T>.logError() =
  when (val e = exceptionOrNull()) {
    is CancellationException -> throw e
    else -> {
      Timber.e(e)
    }
  }
