package io.krugosvet.dailydish.android.architecture.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.krugosvet.dailydish.android.architecture.extension.LiveEvent
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.NavigationEvent
import androidx.lifecycle.ViewModel as JetpackViewModel

abstract class ViewModel<TNavigation : NavigationEvent>(
  val savedStateHandle: SavedStateHandle
) :
  JetpackViewModel() {

  abstract class NavigationEvent

  sealed class State {

    object Loading : State()

    object Inert : State()
  }

  val navigationEvent: LiveData<TNavigation> by lazy { _navigationEvent }

  val state: LiveData<State> by lazy { _state }

  private val _navigationEvent = LiveEvent<TNavigation>()

  private val _state = MutableLiveData<State>()

  protected fun navigate(event: TNavigation) = _navigationEvent.postValue(event)

  protected fun setState(state: State) = _state.postValue(state)
}
