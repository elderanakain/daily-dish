package io.krugosvet.dailydish.android.screen.container.viewmodel

import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.screen.container.viewmodel.ContainerViewModel.Event

class ContainerViewModel :
  ViewModel<Event>() {

  sealed class Event: NavigationEvent() {
    object ShowAddMeal: Event()
  }

  fun onOpenAddMealScreen() {
    navigate(Event.ShowAddMeal)
  }
}
