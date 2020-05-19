package io.krugosvet.dailydish.android.architecture.aspect

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface IDisposableAspect {

  fun store(disposable: Disposable)

  fun clearDisposables()

  fun Disposable.storeDisposable() = store(this)
}

class DisposableAspect: IDisposableAspect {

  private val disposableStorage = CompositeDisposable()

  override fun store(disposable: Disposable) {
    disposableStorage.add(disposable)
  }

  override fun clearDisposables() {
    disposableStorage.clear()
  }
}
