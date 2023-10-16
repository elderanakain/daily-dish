package io.krugosvet.dailydish.android.architecture.extension

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LiveEvent<T> : MutableLiveData<T>() {

    private val isPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            throw IllegalStateException("Already have one observer")
        }

        super.observe(owner, {
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

@Suppress("unused")
fun <T> ViewModel<*>.savedStateFlow(initialValue: T): SavedStateFlow<T> =
    SavedStateFlow(initialValue)

private val gson = Gson()

class SavedStateFlow<T>(
    private val initialValue: T
) :
    ReadOnlyProperty<ViewModel<*>, MutableStateFlow<T>> {

    private lateinit var _field: MutableStateFlow<T>

    override fun getValue(thisRef: ViewModel<*>, property: KProperty<*>): MutableStateFlow<T> =
        synchronized(this) {
            _field = when {
                ::_field.isInitialized -> _field
                else -> thisRef.createStateFlow(property)
            }

            return@synchronized _field
        }

    private fun ViewModel<*>.createStateFlow(property: KProperty<*>) =
        MutableStateFlow(savedStateHandle.get(property) ?: initialValue)
            .also { flow ->
                flow
                    .onEach { newValue ->
                        savedStateHandle[property.name] = Gson().toJson(newValue)
                    }
                    .launchIn(viewModelScope)
            }

    private fun SavedStateHandle.get(property: KProperty<*>): T =
        gson.fromJson(get<String>(property.name), initialValue!!::class.java)
}
