package io.krugosvet.dailydish.android.architecture.extension

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> ViewModel<*>.liveData(initialValue: T) =
  object : ReadOnlyProperty<Any, NonNullMutableLiveData<T>> {

    private val liveData = NonNullMutableLiveData(initialValue)

    override fun getValue(thisRef: Any, property: KProperty<*>) = liveData
  }

fun <T> ViewModel<*>.stateLiveData(initialValue: T) =
  object : ReadOnlyProperty<Any, MutableLiveData<T>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableLiveData<T> =
      this@stateLiveData.savedStateHandle.getLiveData(property.name, initialValue)
  }

open class NonNullMutableLiveData<T>(
  private val initialValue: T
) :
  MutableLiveData<T>() {

  override fun getValue() = super.getValue() ?: initialValue
}

class LiveEvent<T> : MutableLiveData<T>() {

  private val isPending: AtomicBoolean = AtomicBoolean(false)

  @MainThread
  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    if (hasActiveObservers()) {
      throw IllegalStateException("Already have one observer")
    }

    super.observe(owner, Observer {
      if (isPending.compareAndSet(true, false)) {
        observer.onChanged(it)
      }
    })
  }

  @MainThread
  override fun setValue(@Nullable t: T?) {
    isPending.set(true)
    super.setValue(t)
  }
}
