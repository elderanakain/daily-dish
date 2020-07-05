package io.krugosvet.dailydish.android.architecture.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import io.krugosvet.dailydish.android.architecture.extension.LiveEvent
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.NavigationEvent
import androidx.lifecycle.ViewModel as JetpackViewModel

abstract class ViewModel<TNavigation : NavigationEvent>(
  val savedStateHandle: SavedStateHandle
) :
  JetpackViewModel() {

  abstract class NavigationEvent

  val navigationEvent: LiveData<TNavigation>
    get() = mutableNavigationEvent

  private val mutableNavigationEvent = LiveEvent<TNavigation>()

  protected fun navigate(event: TNavigation) = mutableNavigationEvent.postValue(event)
}
