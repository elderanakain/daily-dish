package io.krugosvet.dailydish.android.architecture.extension

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

fun <T> Single<T>.subscribeOnIoThread(
  onSuccess: (T) -> Unit = { defaultOnSuccess(it) },
  onError: (Throwable?) -> Unit = { defaultOnError(it) }
): Disposable =
  subscribeOn(Schedulers.io())
    .subscribe(
      { onSuccess.invoke(it) },
      { onError.invoke(it) }
    )

private fun defaultOnError(error: Throwable?) {
    Timber.e(error, "onDefaultError()")
}

private fun <T> defaultOnSuccess(value: T) {
    Timber.d("onDefaultSuccess(): $value")
}
