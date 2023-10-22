package io.krugosvet.dailydish.android.architecture

import androidx.lifecycle.ViewModel as JetpackViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.ViewModel.NavigationEvent
import io.krugosvet.dailydish.android.errorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ViewModel<TNavigation : NavigationEvent>(
    val savedStateHandle: SavedStateHandle,
) :
    JetpackViewModel() {

    interface NavigationEvent

    sealed interface State {

        data object Loading : State
    }

    private val _navigationEvent = MutableSharedFlow<TNavigation>()
    val navigationEvent: Flow<TNavigation> = _navigationEvent.asSharedFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    protected fun navigate(event: TNavigation) = viewModelScope.launch {
        _navigationEvent.emit(event)
    }

    protected fun setState(state: State) = _state.update { state }

    protected inline fun CoroutineScope.launchCatching(
        crossinline block: suspend CoroutineScope.() -> Unit,
    ): Job =
        launch(coroutineContext + errorHandler) {
            block(this)
        }
}
