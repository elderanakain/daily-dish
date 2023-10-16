package io.krugosvet.dailydish.android.architecture.aspect

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job

interface IStorageAspect<T> {

    fun T.store()

    fun clear()
}

class CoroutineJobAspect : IStorageAspect<Job> {

    private val jobStorage = mutableListOf<Job>()

    override fun Job.store() {
        jobStorage.add(this)
    }

    override fun clear() {
        jobStorage
            .onEach { it.cancel() }
            .clear()
    }
}

class DisposableAspect : IStorageAspect<Disposable> {

    private val disposableStorage = CompositeDisposable()

    override fun Disposable.store() {
        disposableStorage.add(this)
    }

    override fun clear() {
        disposableStorage.clear()
    }
}
