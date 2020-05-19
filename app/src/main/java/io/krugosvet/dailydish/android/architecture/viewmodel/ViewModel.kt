package io.krugosvet.dailydish.android.architecture.viewmodel

import androidx.lifecycle.LiveData
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IDisposableAspect
import io.krugosvet.dailydish.android.architecture.extension.LiveEvent
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.NavigationEvent
import org.koin.core.KoinComponent
import androidx.lifecycle.ViewModel as JetpackViewModel

abstract class ViewModel<TNavigation : NavigationEvent> :
  JetpackViewModel(),
  KoinComponent,
  IDisposableAspect by DisposableAspect() {

  abstract class NavigationEvent

  val navigationEvent: LiveData<TNavigation>
    get() = mutableNavigationEvent

  private val mutableNavigationEvent = LiveEvent<TNavigation>()

  protected fun navigate(event: TNavigation) = mutableNavigationEvent.postValue(event)

  override fun onCleared() {
    super.onCleared()

    clearDisposables()
  }
}
