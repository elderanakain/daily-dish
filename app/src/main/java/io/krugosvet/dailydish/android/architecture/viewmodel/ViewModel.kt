package io.krugosvet.dailydish.android.architecture.viewmodel

import androidx.lifecycle.LiveData
import io.krugosvet.dailydish.android.architecture.aspect.CoroutineJobAspect
import io.krugosvet.dailydish.android.architecture.aspect.IStorageAspect
import io.krugosvet.dailydish.android.architecture.extension.LiveEvent
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.NavigationEvent
import kotlinx.coroutines.Job
import androidx.lifecycle.ViewModel as JetpackViewModel

abstract class ViewModel<TNavigation : NavigationEvent> :
  JetpackViewModel() {

  abstract class NavigationEvent

  val navigationEvent: LiveData<TNavigation>
    get() = mutableNavigationEvent

  private val mutableNavigationEvent = LiveEvent<TNavigation>()

  protected fun navigate(event: TNavigation) = mutableNavigationEvent.postValue(event)
}
