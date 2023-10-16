package io.krugosvet.dailydish.android.ui.container.viewmodel

import androidx.lifecycle.SavedStateHandle
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.ui.container.viewmodel.ContainerViewModel.Event

class ContainerViewModel(
    savedStateHandle: SavedStateHandle
) :
    ViewModel<Event>(savedStateHandle) {

    sealed class Event : NavigationEvent() {
        object ShowAddMeal : Event()
    }

    fun onOpenAddMealScreen() {
        navigate(Event.ShowAddMeal)
    }
}
